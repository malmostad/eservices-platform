(function() {
  var Assert, DateTime, Document, OD, Test, YD;
  OD = ORBEON.util.Dom;
  Test = ORBEON.util.Test;
  Document = ORBEON.xforms.Document;
  Assert = YAHOO.util.Assert;
  YD = YAHOO.util.Dom;
  DateTime = ORBEON.util.DateTime;
  YAHOO.tool.TestRunner.add(new YAHOO.tool.TestCase({
    name: "Date control (including on iOS)",
    isIOS: function() {
      return YD.hasClass(document.body, "xforms-ios");
    },
    checkValues: function(iteration) {
      var controlId, controlIds, expectedValue, input, inputControl, iso, isoDateTimes, outputControl, outputValue, type, _i, _len, _ref, _results;
      controlIds = ["date", "time", "dateTime"];
      _results = [];
      for (_i = 0, _len = controlIds.length; _i < _len; _i++) {
        controlId = controlIds[_i];
        _ref = (function() {
          var _i, _len, _ref, _results;
          _ref = ["input", "output"];
          _results = [];
          for (_i = 0, _len = _ref.length; _i < _len; _i++) {
            type = _ref[_i];
            _results.push(YD.get(controlId + "-" + type + "⊙" + iteration));
          }
          return _results;
        })(), inputControl = _ref[0], outputControl = _ref[1];
        outputValue = Document.getValue(outputControl);
        isoDateTimes = outputValue.split("T");
        _results.push((function() {
          var _i, _len, _ref, _results;
          _ref = inputControl.getElementsByTagName("input");
          _results = [];
          for (_i = 0, _len = _ref.length; _i < _len; _i++) {
            input = _ref[_i];
            expectedValue = YD.hasClass(input, "xforms-type-date") ? (iso = isoDateTimes[0], this.isIOS() ? iso : DateTime.jsDateToFormatDisplayDate(DateTime.magicDateToJSDate(iso))) : YD.hasClass(input, "xforms-type-time") ? (iso = isoDateTimes[isoDateTimes.length - 1], this.isIOS() ? iso : DateTime.jsDateToFormatDisplayTime(DateTime.magicTimeToJSDate(iso))) : void 0;
            _results.push(Assert.areEqual(expectedValue, input.value));
          }
          return _results;
        }).call(this));
      }
      return _results;
    },
    testInitialValue: function() {
      return Test.runMayCauseXHR(this, function() {
        return this.checkValues("1");
      }, function() {
        return Test.click("add");
      }, function() {
        return this.checkValues("2");
      }, function() {
        return Test.click("remove");
      });
    },
    testValueAfterIncrement: function() {
      return Test.runMayCauseXHR(this, function() {
        return Test.click("increment-date-time⊙1");
      }, function() {
        return this.checkValues("1");
      }, function() {
        return Test.click("add");
      }, function() {
        return Test.click("increment-date-time⊙2");
      }, function() {
        return this.checkValues("2");
      }, function() {
        return Test.click("remove");
      }, function() {
        return Test.click("reset-date-time⊙1");
      });
    }
  }));
  Test.onOrbeonLoadedRunTest();
}).call(this);
