(function() {
  var Assert, Document, OD, Test, YD;
  OD = ORBEON.util.Dom;
  Test = ORBEON.util.Test;
  Document = ORBEON.xforms.Document;
  Assert = YAHOO.util.Assert;
  YD = YAHOO.util.Dom;
  YAHOO.tool.TestRunner.add(new YAHOO.tool.TestCase({
    name: "Input placeholder",
    browserSupportsPlaceholder: (function() {
      var input;
      input = document.createElement("input");
      return input.placeholder != null;
    })(),
    assertPlaceholderShown: function(control, placeholder) {
      var input;
      input = (control.getElementsByTagName("input"))[0];
      Assert.areEqual(placeholder, input.placeholder, "placeholder attribute has the correct value");
      if (this.browserSupportsPlaceholder) {
        return Assert.areEqual("", input.value, "input should have no value");
      } else {
        Assert.isTrue(YD.hasClass(control, "xforms-placeholder"), "has placeholder class");
        return Assert.areEqual(placeholder, input.value, "input should have placeholder value");
      }
    },
    assertContentShown: function(control, content) {
      var input;
      input = (control.getElementsByTagName("input"))[0];
      Assert.isFalse(YD.hasClass(control, "xforms-placeholder"), "doesn't have placeholder class");
      return Assert.areEqual(content, input.value, "input must show content");
    },
    assertBlockShows: function(index, values) {
      var control, i, prefixes, value, _results;
      prefixes = ["static-label" + XF_REPEAT_SEPARATOR, "static-hint" + XF_REPEAT_SEPARATOR, "dynamic-label" + XF_REPEAT_SEPARATOR, "dynamic-hint" + XF_REPEAT_SEPARATOR];
      _results = [];
      for (i = 0; i <= 3; i++) {
        value = values[i];
        control = YD.get(prefixes[i] + index);
        if (value.placeholder != null) {
          this.assertPlaceholderShown(control, value.placeholder);
        }
        _results.push(value.content != null ? this.assertContentShown(control, value.content) : void 0);
      }
      return _results;
    },
    testPlaceholderShown: function() {
      var placeholders;
      placeholders = [
        {
          placeholder: "First name"
        }, {
          placeholder: "First name"
        }, {
          placeholder: "1"
        }, {
          placeholder: "1"
        }
      ];
      return Test.runMayCauseXHR(this, function() {
        return this.assertBlockShows(1, placeholders);
      }, function() {
        return Test.click("add");
      }, function() {
        return this.assertBlockShows(2, placeholders);
      }, function() {
        return Test.click("remove");
      });
    },
    testContentShown: function() {
      var content;
      content = [
        {
          content: "1"
        }, {
          content: "1"
        }, {
          content: "1"
        }, {
          content: "1"
        }
      ];
      return Test.runMayCauseXHR(this, function() {
        return Test.click("increment-content" + XF_REPEAT_SEPARATOR + "1");
      }, function() {
        return this.assertBlockShows(1, content);
      }, function() {
        return Test.click("reset-content" + XF_REPEAT_SEPARATOR + "1");
      }, function() {
        return Test.click("add");
      }, function() {
        return Test.click("increment-content" + XF_REPEAT_SEPARATOR + "2");
      }, function() {
        return this.assertBlockShows(2, content);
      }, function() {
        return Test.click("remove");
      });
    },
    testFocusNoPlaceholder: function() {
      var focusOnFirst;
      focusOnFirst = [
        {
          content: ""
        }, {
          placeholder: "First name"
        }, {
          placeholder: "1"
        }, {
          placeholder: "1"
        }
      ];
      return Test.runMayCauseXHR(this, function() {
        return (YD.get("static-label" + XF_COMPONENT_SEPARATOR + "xforms-input-1" + XF_REPEAT_SEPARATOR + "1")).focus();
      }, function() {
        return this.assertBlockShows(1, focusOnFirst);
      }, function() {
        return Test.click("add");
      }, function() {
        return (YD.get("static-label" + XF_COMPONENT_SEPARATOR + "xforms-input-1" + XF_REPEAT_SEPARATOR + "2")).focus();
      }, function() {
        return this.assertBlockShows(2, focusOnFirst);
      }, function() {
        return Test.click("remove");
      }, function() {
        return (YD.get("add")).focus();
      });
    }
  }));
  Test.onOrbeonLoadedRunTest();
}).call(this);
