(function() {
  var AjaxServer, Builder, Controls, Event, Events, YD, relevanceRules;
  AjaxServer = ORBEON.xforms.server.AjaxServer;
  Builder = ORBEON.Builder;
  Controls = ORBEON.xforms.Controls;
  Event = YAHOO.util.Event;
  Events = ORBEON.xforms.Events;
  YD = YAHOO.util.Dom;
  relevanceRules = (function() {
    var isEmptyUpload, isNotEmpty, isUpload;
    isNotEmpty = function(gridTd) {
      return $(gridTd).find('.fr-grid-content').children().length > 0;
    };
    isUpload = function(gridTd) {
      return isNotEmpty(gridTd) && $(gridTd).find('.fr-grid-content').hasClass('fb-upload');
    };
    isEmptyUpload = function(gridTd) {
      var photo, spacer;
      spacer = '/ops/images/xforms/spacer.gif';
      photo = '/apps/fr/style/images/silk/photo.png';
      return $(gridTd).find(".xforms-output-output[src $= '" + spacer + "'], .xforms-output-output[src $= '" + photo + "']").length > 0;
    };
    return {
      'fb-expand-trigger': function(gridTd) {
        var grid, gridTr, noDelimiter, oddEvenClass;
        noDelimiter = ':not(.xforms-repeat-delimiter):not(.xforms-repeat-template):not(.xforms-repeat-begin-end):not(.xforms-group-begin-end)';
        grid = $(gridTd).closest('.fr-grid');
        if (grid.hasClass('fr-norepeat')) {
          return gridTd.rowSpan <= $(gridTd).parent().nextAll('tr' + noDelimiter).length;
        } else if (grid.hasClass('fr-repeat-multiple-rows')) {
          gridTr = $(gridTd).parent();
          oddEvenClass = gridTr.hasClass('yui-dt-even') ? 'yui-dt-even' : 'yui-dt-odd';
          return gridTd.rowSpan <= gridTr.next('.' + oddEvenClass).length;
        } else if (grid.hasClass('fr-repeat-single-row')) {
          return false;
        } else {
          return false;
        }
      },
      'fb-shrink-trigger': function(gridTd) {
        return gridTd.rowSpan >= 2;
      },
      'fb-delete-trigger': isNotEmpty,
      'fb-edit-details-trigger': isNotEmpty,
      'fb-edit-validation-trigger': isNotEmpty,
      'fb-edit-items-trigger': function(gridTd) {
        return isNotEmpty(gridTd) && $(gridTd).find('.fr-grid-content').hasClass('fb-itemset');
      },
      'fb-static-upload-empty': function(gridTd) {
        return isUpload(gridTd) && isEmptyUpload(gridTd);
      },
      'fb-static-upload-non-empty': function(gridTd) {
        return isUpload(gridTd) && !isEmptyUpload(gridTd);
      }
    };
  })();
  Builder.beforeAddingEditorCallbacks = $.Callbacks();
  $(function() {
    var createHoverDiv, lastPositionTriggers;
    lastPositionTriggers = null;
    createHoverDiv = function(gridThTd) {
      var hoverDiv;
      hoverDiv = $(gridThTd).children('.fb-hover');
      if (hoverDiv.length === 0) {
        hoverDiv = $('<div>').addClass('fb-hover');
        hoverDiv.append($(gridThTd).children());
        $(gridThTd).append(hoverDiv);
      }
      return hoverDiv;
    };
    Builder.mouseEntersGridTdEvent.subscribe(function(_arg) {
      var gridTd, gridTdId, triggerGroups, triggers;
      triggers = _arg.triggers, triggerGroups = _arg.triggerGroups, gridTd = _arg.gridTd;
      gridTdId = gridTd.id;
      lastPositionTriggers = function() {
        var hoverDiv, rule, trigger, triggerRelevant, _i, _len, _results;
        gridTd = YD.get(gridTdId);
        if (gridTd != null) {
          hoverDiv = createHoverDiv(gridTd);
          hoverDiv.append(triggerGroups);
          $(triggerGroups).css('display', '');
          _results = [];
          for (_i = 0, _len = triggers.length; _i < _len; _i++) {
            trigger = triggers[_i];
            rule = relevanceRules[trigger.id];
            triggerRelevant = rule != null ? rule(gridTd) : true;
            _results.push(trigger.style.display = triggerRelevant ? '' : 'none');
          }
          return _results;
        }
      };
      return lastPositionTriggers();
    });
    tinyMCE.onAddEditor.add(function(sender, editor) {
      return Builder.beforeAddingEditorCallbacks.fire($(document.getElementById(editor.id)));
    });
    Builder.beforeAddingEditorCallbacks.add(function(editor) {
      var gridThTd;
      gridThTd = f$.closest('th.fb-grid-th, td.fb-grid-td', editor);
      return createHoverDiv(gridThTd);
    });
    Builder.mouseExitsGridTdEvent.subscribe(function(_arg) {
      var gridTd, triggerGroups;
      triggerGroups = _arg.triggerGroups, gridTd = _arg.gridTd;
      $(triggerGroups).hide();
      $('.fb-cell-editor').append(triggerGroups);
      return lastPositionTriggers = null;
    });
    Builder.triggerClickEvent.subscribe(function(_arg) {
      var activable, event, form, trigger;
      trigger = _arg.trigger;
      activable = YD.getAncestorByClassName(trigger, "xforms-activable");
      form = Controls.getForm(activable);
      event = new AjaxServer.Event(form, activable.id, null, "DOMActivate");
      return AjaxServer.fireEvents([event]);
    });
    return Events.ajaxResponseProcessedEvent.subscribe(function() {
      if (lastPositionTriggers != null) {
        return lastPositionTriggers();
      }
    });
  });
}).call(this);
