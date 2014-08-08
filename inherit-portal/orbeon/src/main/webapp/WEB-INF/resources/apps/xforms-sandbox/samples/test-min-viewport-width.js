(function() {
  var Assert, Document, OD, Test, YD;
  OD = ORBEON.util.Dom;
  Test = ORBEON.util.Test;
  Document = ORBEON.xforms.Document;
  Assert = YAHOO.util.Assert;
  YD = YAHOO.util.Dom;
  YAHOO.tool.TestRunner.add(new YAHOO.tool.TestCase({
    name: "Viewport minimum width",
    testNoScroll: function() {
      var region, topLevel, topLevelContainer, _i, _len, _ref;
      topLevelContainer = document.createElement("div");
      topLevelContainer.className = "test-top-level-div";
      _ref = YD.getChildren(document.body);
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        topLevel = _ref[_i];
        if (!YD.hasClass(topLevel, "yui-log")) {
          topLevelContainer.appendChild(topLevel);
        }
      }
      document.body.appendChild(topLevelContainer);
      region = YD.getRegion(topLevelContainer);
      Assert.isTrue(topLevelContainer.clientHeight >= topLevelContainer.scrollHeight, "Scroll height should be same as client height");
      return Assert.isTrue(topLevelContainer.clientWidth >= topLevelContainer.scrollWidth, "Scroll width should be the same as client width");
    }
  }));
  Test.onOrbeonLoadedRunTest();
}).call(this);
