(function() {
  var Assert, Document, OD, Test, YD;
  OD = ORBEON.util.Dom;
  Test = ORBEON.util.Test;
  Document = ORBEON.xforms.Document;
  Assert = YAHOO.util.Assert;
  YD = YAHOO.util.Dom;
  YAHOO.tool.TestRunner.add(new YAHOO.tool.TestCase({
    name: "Full update",
    testGroupAroundTr: function() {
      var button, groupBegin;
      groupBegin = document.getElementById("group-begin-group-update-full" + XF_REPEAT_SEPARATOR + "2");
      button = OD.getElementByTagName(OD.get("toggle-two"), "button");
      return Test.executeSequenceCausingAjaxRequest(this, [
        [
          function() {
            return button.click();
          }, function() {
            var tr;
            tr = YD.getNextSibling(groupBegin);
            Assert.areEqual("tr", tr.tagName.toLowerCase());
            return Assert.isTrue(YD.hasClass(tr, "xforms-disabled"));
          }
        ], [
          function() {
            return button.click();
          }, function() {
            var tr;
            tr = YD.getNextSibling(groupBegin);
            Assert.areEqual("tr", tr.tagName.toLowerCase());
            return Assert.isFalse(YD.hasClass(tr, "xforms-disabled"));
          }
        ]
      ]);
    },
    testCase: function() {
      var button, caseBegin;
      caseBegin = document.getElementById("xforms-case-begin-case-1");
      button = OD.getElementByTagName(OD.get("increment-case-value"), "button");
      return Test.executeSequenceCausingAjaxRequest(this, [
        [
          function() {
            return button.click();
          }, function() {
            var span;
            span = YD.getNextSibling(caseBegin);
            Assert.isTrue(YD.hasClass(span, "xforms-control"));
            return Assert.areEqual("2", ORBEON.xforms.Controls.getCurrentValue(span));
          }
        ]
      ]);
    },
    testRestoreFocus: function() {
      return Test.executeSequenceCausingAjaxRequest(this, [
        [
          function() {
            var focusRestoreInput;
            focusRestoreInput = OD.getElementByTagName(OD.get("focus-restore"), "input");
            return focusRestoreInput.focus();
          }, function() {
            return Assert.areEqual(ORBEON.xforms.Globals.currentFocusControlElement, document.getElementById("focus-restore"), "focus is restored to first input box");
          }
        ]
      ]);
    },
    testFocusNonRelevantNoError: function() {
      return Test.executeSequenceCausingAjaxRequest(this, [
        [
          function() {
            var nonRelevantInput;
            nonRelevantInput = OD.getElementByTagName(OD.get("focus-non-relevant-no-error"), "input");
            return nonRelevantInput.focus();
          }
        ]
      ]);
    },
    testFocusReadonlyNoError: function() {
      return Test.executeSequenceCausingAjaxRequest(this, [
        [
          function() {
            var readonlyInput;
            readonlyInput = OD.getElementByTagName(OD.get("focus-readonly-no-error"), "input");
            return readonlyInput.focus();
          }
        ]
      ]);
    },
    testServerValueUserChangeSent: function() {
      return Test.executeSequenceCausingAjaxRequest(this, [
        [
          function() {
            return Document.setValue("server-value-input", "true");
          }, function() {
            return Assert.areEqual("true", Document.getValue("server-value-output"), "first set checkbox to true");
          }
        ], [
          function() {
            return Test.click("server-value-false");
          }, function() {
            return Assert.areEqual("false", Document.getValue("server-value-output"), "false set with setvalue");
          }
        ], [
          function() {
            return Document.setValue("server-value-input", "true");
          }, function() {
            return Assert.areEqual("true", Document.getValue("server-value-output"), "second set checkbox to true");
          }
        ], [
          function() {
            return Test.click("server-value-false");
          }, function() {
            return Assert.areEqual("false", Document.getValue("server-value-output"), "reset to false to get back to initial state");
          }
        ]
      ]);
    },
    testDialogInitialized: function() {
      return Test.executeSequenceCausingAjaxRequest(this, [
        [
          function() {
            var addIteration;
            addIteration = OD.getElementByTagName(OD.get("add-iteration"), "button");
            return addIteration.click();
          }, function() {
            return Assert.isObject(ORBEON.xforms.Globals.dialogs["dialog" + XF_REPEAT_SEPARATOR + "1"]);
          }
        ]
      ]);
    }
  }));
  Test.onOrbeonLoadedRunTest();
}).call(this);
