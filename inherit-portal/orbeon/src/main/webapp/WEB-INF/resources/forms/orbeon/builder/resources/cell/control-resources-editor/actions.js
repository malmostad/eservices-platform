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
  */  var Builder, OD, annotateWithLhhaClass, htmlClass, isLabelHintHtml, labelHintEditor, labelHintValue, lhha, setLabelHintHtml;
  Builder = ORBEON.Builder;
  OD = ORBEON.xforms.Document;
  Builder.resourceEditorCurrentControl = null;
  Builder.resourceEditorCurrentLabelHint = null;
  lhha = function() {
    if (Builder.resourceEditorCurrentLabelHint.is('.xforms-label')) {
      return 'label';
    } else {
      return 'hint';
    }
  };
  htmlClass = function() {
    return 'fb-' + lhha() + '-is-html';
  };
  isLabelHintHtml = function() {
    return Builder.resourceEditorCurrentControl.is('.' + htmlClass());
  };
  setLabelHintHtml = function(isHtml) {
    return Builder.resourceEditorCurrentControl.toggleClass(htmlClass(), isHtml);
  };
  annotateWithLhhaClass = function(add) {
    return labelHintEditor().container.toggleClass('fb-label-editor-for-' + lhha(), add);
  };
  labelHintValue = function(value) {
    var valueAccessor;
    valueAccessor = isLabelHintHtml() ? Builder.resourceEditorCurrentLabelHint.html : Builder.resourceEditorCurrentLabelHint.text;
    valueAccessor = _.bind(valueAccessor, Builder.resourceEditorCurrentLabelHint);
    if (value != null) {
      return valueAccessor(value);
    } else {
      return valueAccessor();
    }
  };
  labelHintEditor = _.memoize(function() {
    var editor;
    editor = {};
    editor.textfield = $('<input type="text">');
    editor.checkbox = $('<input type="checkbox">');
    editor.container = $('<div class="fb-label-editor">').append(editor.textfield).append(editor.checkbox);
    editor.container.hide();
    $('.fb-main').append(editor.container);
    editor.checkbox.on('click', function() {
      return labelHintEditor().textfield.focus();
    });
    editor.textfield.on('keypress', function(e) {
      if (e.which === 13) {
        return Builder.resourceEditorEndEdit();
      }
    });
    return editor;
  });
  Builder.resourceEditorStartEdit = function() {
    var labelHintOffset;
    Builder.resourceEditorCurrentLabelHint.removeAttr('for');
    labelHintOffset = Builder.resourceEditorCurrentLabelHint.offset();
    labelHintEditor().container.show();
    labelHintEditor().container.offset(labelHintOffset);
    labelHintEditor().container.width(Builder.resourceEditorCurrentLabelHint.outerWidth());
    labelHintEditor().textfield.outerWidth(Builder.resourceEditorCurrentLabelHint.outerWidth() - labelHintEditor().checkbox.outerWidth(true));
    labelHintEditor().textfield.val(labelHintValue()).focus();
    labelHintEditor().checkbox.prop('checked', isLabelHintHtml());
    labelHintEditor().checkbox.tooltip({
      title: $('.fb-message-lhha-checkbox').text()
    });
    labelHintEditor().textfield.attr('placeholder', $(".fb-message-type-" + (lhha())).text());
    Builder.resourceEditorCurrentLabelHint.css('visibility', 'hidden');
    return annotateWithLhhaClass(true);
  };
  Builder.resourceEditorEndEdit = function() {
    var isChecked, newValue;
    if (labelHintEditor().container.is(':visible')) {
      newValue = labelHintEditor().textfield.val();
      isChecked = labelHintEditor().checkbox.is(':checked');
      OD.dispatchEvent({
        targetId: Builder.resourceEditorCurrentControl.attr('id'),
        eventName: 'fb-update-control-lhha',
        properties: {
          lhha: lhha(),
          value: newValue,
          isHtml: isChecked.toString()
        }
      });
      labelHintEditor().checkbox.tooltip('destroy');
      labelHintEditor().container.hide();
      annotateWithLhhaClass(false);
      Builder.resourceEditorCurrentLabelHint.css('visibility', '');
      setLabelHintHtml(isChecked);
      labelHintValue(newValue);
      Builder.resourceEditorCurrentControl = null;
      return Builder.resourceEditorCurrentLabelHint = null;
    }
  };
}).call(this);
