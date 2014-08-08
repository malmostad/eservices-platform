(function() {
  var Assert, Document, OD, Test, YD;
  OD = ORBEON.util.Dom;
  Test = ORBEON.util.Test;
  Document = ORBEON.xforms.Document;
  Assert = YAHOO.util.Assert;
  YD = YAHOO.util.Dom;
  YAHOO.tool.TestRunner.add(new YAHOO.tool.TestCase({
    name: "Flat repeat",
    testHiddenTemplateShowsWhenInserted: function() {
      var isCatVisible;
      isCatVisible = function(position) {
        var cat;
        cat = YD.get("cat" + XF_REPEAT_SEPARATOR + position);
        return !(_.any(["xforms-disabled", "xforms-disabled-subsequent"], function(c) {
          return YD.hasClass(cat.parentElement, c);
        }));
      };
      return Test.runMayCauseXHR(this, function() {
        return Test.click("show");
      }, function() {
        return Assert.isTrue(isCatVisible(1), "Showing the group, the first cat should be there");
      }, function() {
        return Test.click("add");
      }, function() {
        return Assert.isTrue(isCatVisible(2), "The copy of a template with xforms-disabled must be visible");
      }, function() {
        return Test.click("hide");
      }, function() {
        return Test.click("show");
      }, function() {
        return Test.click("add");
      }, function() {
        return Assert.isTrue(isCatVisible(3), "The copy of a template with xforms-disabled-subsequent must be visible");
      });
    }
  }));
  Test.onOrbeonLoadedRunTest();
}).call(this);
