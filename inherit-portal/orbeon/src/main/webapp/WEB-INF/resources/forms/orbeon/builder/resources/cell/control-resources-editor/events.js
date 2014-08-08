(function() {
  /*
  Copyright (C) 2014 Orbeon, Inc.

  This program is free software; you can redistribute it and/or modify it under the terms of the
  GNU Lesser General Public License as published by the Free Software Foundation; either version
  2.1 of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  See the GNU Lesser General Public License for more details.

  The full text of the license is available at http://www.gnu.org/copyleft/lesser.html
  */  var Builder, ControlSelector, LabelHintSelector, clickOrFocus;
  Builder = ORBEON.Builder;
  LabelHintSelector = '.fr-editable .xforms-label, .fr-editable .xforms-hint, .fr-editable .xforms-text';
  ControlSelector = '.xforms-control, .xbl-component';
  clickOrFocus = function(_arg) {
    var eventOnControlLabel, eventOnEditor, target;
    target = _arg.target;
    target = $(target);
    eventOnEditor = target.closest('.fb-label-editor').is('*');
    eventOnControlLabel = (target.is(LabelHintSelector) || target.parents(LabelHintSelector).is('*')) && target.parents('.fb-main').is('*');
    if (!(eventOnEditor || eventOnControlLabel)) {
      return Builder.resourceEditorEndEdit();
    }
  };
  $(function() {
    $(document).on('click', clickOrFocus);
    $(document).on('focusin', clickOrFocus);
    $('.fb-main').on('click', LabelHintSelector, function(_arg) {
      var currentTarget, tdWithControl, th, trWithControls;
      currentTarget = _arg.currentTarget;
      if (!_.isNull(Builder.resourceEditorCurrentControl)) {
        Builder.resourceEditorEndEdit();
      }
      Builder.resourceEditorCurrentLabelHint = $(currentTarget);
      th = Builder.resourceEditorCurrentLabelHint.parents('th');
      Builder.resourceEditorCurrentControl = th.is('*') ? (trWithControls = th.parents('table').find('tbody tr.fb-grid-tr').first(), tdWithControl = trWithControls.children(':nth-child(' + (th.index() + 1) + ')'), tdWithControl.find(ControlSelector)) : Builder.resourceEditorCurrentLabelHint.parents(ControlSelector).first();
      return Builder.resourceEditorStartEdit();
    });
    return Builder.controlAdded.add(function(containerId) {
      var container, repeat;
      container = $(document.getElementById(containerId));
      Builder.resourceEditorCurrentControl = container.find(ControlSelector);
      repeat = container.parents('.fr-repeat').first();
      Builder.resourceEditorCurrentLabelHint = repeat.is('*') ? repeat.find('thead tr th:nth-child(' + (container.index() + 1) + ') .xforms-label') : container.find('.xforms-label');
      if (Builder.resourceEditorCurrentLabelHint.is('*')) {
        return Builder.resourceEditorStartEdit();
      }
    });
  });
}).call(this);
