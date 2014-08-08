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
  */
  /*
  Show placeholders, e.g. "Click here to…"
  - Maintain on the <label class="xforms-label"> and <span class="xforms-hint"> an attribute placeholder.
  - Value of placeholder attribute is the text shown on grid mouseover if the label/hint is empty.
  - The value of the placeholder is shown with CSS :empty:before.
  */  var Builder, Events, gridTdUpdated, placeholderTextForResource, resourceNames, updateGridTd, updatePlacehodlerText;
  Builder = ORBEON.Builder;
  Events = ORBEON.xforms.Events;
  resourceNames = ['label', 'hint', 'text'];
  gridTdUpdated = [];
  placeholderTextForResource = {};
  updateGridTd = function(gridTd) {
    gridTd = $(gridTd);
    return _.each(resourceNames, function(lhha) {
      var elementInDiv;
      elementInDiv = gridTd.find(".xforms-" + lhha);
      if (elementInDiv.is('.xforms-output')) {
        elementInDiv = elementInDiv.children('.xforms-output-output');
      }
      return elementInDiv.attr('placeholder', placeholderTextForResource[lhha]);
    });
  };
  updatePlacehodlerText = function() {
    var foundDifference, isInDoc;
    foundDifference = false;
    _.each(resourceNames, function(lhha) {
      var newText;
      newText = $(".fb-message-enter-" + lhha).children().text();
      if (newText !== placeholderTextForResource[lhha]) {
        placeholderTextForResource[lhha] = newText;
        return foundDifference = true;
      }
    });
    if (foundDifference) {
      isInDoc = function(e) {
        return $.contains(document.body, e);
      };
      gridTdUpdated = _.filter(gridTdUpdated, isInDoc);
      return _.each(gridTdUpdated, updateGridTd);
    }
  };
  $(function() {
    updatePlacehodlerText();
    Events.ajaxResponseProcessedEvent.subscribe(updatePlacehodlerText);
    return Builder.mouseEntersGridTdEvent.subscribe(function(_arg) {
      var gridTd;
      gridTd = _arg.gridTd;
      if (!_.contains(gridTdUpdated, gridTd)) {
        gridTdUpdated.push(gridTd);
        return updateGridTd(gridTd);
      }
    });
  });
}).call(this);
