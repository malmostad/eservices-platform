(function() {
  $(function() {
    var AjaxServer, Builder, Controls, Events, FSM, OD, Properties, currentSection, frBodyLeft, pageX, pageY, sectionEditor, sectionsCache, setupLabelEditor;
    AjaxServer = ORBEON.xforms.server.AjaxServer;
    Builder = ORBEON.Builder;
    Controls = ORBEON.xforms.Controls;
    Events = ORBEON.xforms.Events;
    FSM = ORBEON.util.FiniteStateMachine;
    OD = ORBEON.xforms.Document;
    Properties = ORBEON.util.Properties;
    sectionEditor = $('.fb-section-editor');
    currentSection = null;
    sectionsCache = [];
    frBodyLeft = 0;
    pageX = 0;
    pageY = 0;
    FSM.create({
      transitions: [
        {
          events: ['mouseMove'],
          actions: ['mouseMoved']
        }
      ],
      events: {
        mouseMove: function(f) {
          return ($(document)).on('mousemove', function(e) {
            return f(e);
          });
        }
      },
      actions: {
        mouseMoved: function(event) {
          pageX = event.pageX;
          return pageY = event.pageY;
        }
      }
    });
    Builder.onOffsetMayHaveChanged(function() {
      frBodyLeft = (f$.offset($('.fr-body'))).left;
      sectionsCache.length = 0;
      return _.each($('.xbl-fr-section:visible'), function(section) {
        section = $(section);
        return sectionsCache.unshift({
          el: section,
          offset: Builder.adjustedOffset(section),
          height: f$.height(section),
          titleOffset: f$.offset(f$.find('a', section))
        });
      });
    });
    Builder.currentContainerChanged(sectionsCache, {
      wasCurrent: function() {
        sectionEditor.hide();
        return currentSection = null;
      },
      becomesCurrent: function(section) {
        currentSection = section.el;
        (function() {
          sectionEditor.show();
          return sectionEditor.offset({
            top: section.offset.top - Builder.scrollTop(),
            left: frBodyLeft - f$.outerWidth(sectionEditor)
          });
        })();
        return (function() {
          var container, deleteTrigger;
          container = section.el.children('.fr-section-container');
          _.each(['up', 'right', 'down', 'left'], function(direction) {
            var relevant, trigger;
            relevant = container.hasClass("fb-can-move-" + direction);
            trigger = sectionEditor.children('.fb-section-move-' + direction);
            if (relevant) {
              return trigger.show();
            } else {
              return trigger.hide();
            }
          });
          deleteTrigger = f$.children('.delete-section-trigger', sectionEditor);
          if (f$.is('.fb-can-delete', container)) {
            return f$.show(deleteTrigger);
          } else {
            return f$.hide(deleteTrigger);
          }
        })();
      }
    });
    return (setupLabelEditor = function() {
      var labelInput, sendNewLabelValue, showClickHintIfTitleEmpty, showLabelEditor, updateHightlight;
      labelInput = null;
      AjaxServer.beforeSendingEvent.add(function(event, addProperties) {
        var inSectionEditor, target;
        target = $(document.getElementById(event.targetId));
        inSectionEditor = f$.is('*', f$.closest('.fb-section-editor', target));
        if (event.eventName === 'DOMActivate' && inSectionEditor) {
          return addProperties({
            'section-id': f$.attr('id', currentSection)
          });
        }
      });
      sendNewLabelValue = function() {
        var newLabelValue, section, sectionId;
        newLabelValue = f$.val(labelInput);
        section = Builder.findInCache(sectionsCache, (Builder.adjustedOffset(labelInput)).top);
        f$.text(newLabelValue, f$.find('.fr-section-label:first a', section.el));
        sectionId = f$.attr('id', section.el);
        OD.dispatchEvent({
          targetId: sectionId,
          eventName: 'fb-update-section-label',
          properties: {
            label: newLabelValue
          }
        });
        return f$.hide(labelInput);
      };
      showLabelEditor = function(clickInterceptor) {
        var inputOffset, interceptorOffset, labelAnchor;
        if (ORBEON.xforms.Globals.eventQueue.length > 0 || ORBEON.xforms.Globals.requestInProgress) {
          return _.delay((function() {
            return showLabelEditor(clickInterceptor);
          }), Properties.internalShortDelay.get());
        } else {
          f$.text('', clickInterceptor);
          if (labelInput == null) {
            labelInput = $('<input class="fb-edit-section-label"/>');
            f$.append(labelInput, $('.fb-main'));
            labelInput.on('blur', function() {
              if (f$.is(':visible', labelInput)) {
                return sendNewLabelValue();
              }
            });
            labelInput.on('keypress', function(e) {
              if (e.which === 13) {
                return sendNewLabelValue();
              }
            });
            Events.ajaxResponseProcessedEvent.subscribe(function() {
              return f$.hide(labelInput);
            });
          }
          interceptorOffset = Builder.adjustedOffset(clickInterceptor);
          labelAnchor = (function() {
            var section;
            section = Builder.findInCache(sectionsCache, interceptorOffset.top);
            return f$.find('.fr-section-label:first a', section.el);
          })();
          (function() {
            var placeholderOutput, placeholderValue;
            placeholderOutput = f$.children('.fb-type-section-title-label', sectionEditor);
            placeholderValue = Controls.getCurrentValue(placeholderOutput[0]);
            return f$.attr('placeholder', placeholderValue, labelInput);
          })();
          f$.val(f$.text(labelAnchor), labelInput);
          f$.show(labelInput);
          inputOffset = {
            top: interceptorOffset.top - Builder.scrollTop() + ((f$.height(clickInterceptor)) - (f$.height(labelInput))) / 2,
            left: interceptorOffset.left
          };
          f$.offset(inputOffset, labelInput);
          f$.offset(inputOffset, labelInput);
          f$.width((f$.width(labelAnchor)) - 10, labelInput);
          return f$.focus(labelInput);
        }
      };
      updateHightlight = function(updateClass, clickInterceptor) {
        var offset, section, sectionTitle;
        offset = Builder.adjustedOffset(clickInterceptor);
        section = Builder.findInCache(sectionsCache, offset.top);
        sectionTitle = f$.find('.fr-section-title:first', section.el);
        return updateClass('hover', sectionTitle);
      };
      showClickHintIfTitleEmpty = function(clickInterceptor) {
        var hintMessage, interceptorOffset, labelAnchor, outputWithHintMessage, section;
        interceptorOffset = Builder.adjustedOffset(clickInterceptor);
        section = Builder.findInCache(sectionsCache, interceptorOffset.top);
        labelAnchor = f$.find('.fr-section-label:first a', section.el);
        if ((f$.text(labelAnchor)) === '') {
          outputWithHintMessage = sectionEditor.children('.fb-enter-section-title-label');
          hintMessage = Controls.getCurrentValue(outputWithHintMessage.get(0));
          return clickInterceptor.text(hintMessage);
        }
      };
      return (function() {
        var labelClickInterceptors;
        labelClickInterceptors = [];
        return Builder.onOffsetMayHaveChanged(function() {
          var sections;
          sections = $('.xbl-fr-section');
          _.each(_.range(sections.length - labelClickInterceptors.length), function() {
            var container;
            container = $('<div class="fb-section-label-editor-click-interceptor">');
            f$.append(container, $('.fb-main'));
            container.on('click', function(_arg) {
              var target;
              target = _arg.target;
              return showLabelEditor($(target));
            });
            container.on('mouseover', function(_arg) {
              var target;
              target = _arg.target;
              updateHightlight(f$.addClass, $(target));
              return showClickHintIfTitleEmpty($(target));
            });
            container.on('mouseout', function(_arg) {
              var target;
              target = _arg.target;
              updateHightlight(f$.removeClass, $(target));
              return f$.text('', $(target));
            });
            return labelClickInterceptors.push(container);
          });
          _.each(_.range(sections.length, labelClickInterceptors.length), function(pos) {
            return labelClickInterceptors[pos].hide();
          });
          return _.each(_.range(sections.length), function(pos) {
            var interceptor, title;
            title = f$.find('.fr-section-label a', $(sections[pos]));
            interceptor = labelClickInterceptors[pos];
            interceptor.show();
            interceptor.offset(title.offset());
            interceptor.height(title.height());
            return interceptor.width(title.width());
          });
        });
      })();
    })();
  });
}).call(this);
