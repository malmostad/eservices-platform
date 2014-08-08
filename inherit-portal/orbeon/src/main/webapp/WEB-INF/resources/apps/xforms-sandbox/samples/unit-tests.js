(function() {
  var TestManager, page, pages;
  TestManager = YAHOO.tool.TestManager;
  window.TestManager = TestManager;
  pages = ["test-bug-checkbox-update", "test-bug-checkbox-update", "test-control-xhtml-area", "test-custom-mips", "test-deferred-client-events", "test-dialog", "test-disabled-nested", "test-do-update", "test-error-ajax", "test-group-delimiters", "test-keypress", "test-loading-indicator", "test-message", "test-min-viewport-width", "test-orbeon-dom", "test-output-update", "test-repeat", "test-repeat-setvalue", "test-trigger-modal", "test-underscore", "test-update-full", "test-upload-replace-instance", "test-xforms-controls", "xbl/orbeon/accordion/accordion-unittest", "xbl/orbeon/autocomplete/autocomplete-unittest", "xbl/orbeon/button/button-unittest", "xbl/orbeon/currency/currency-unittest", "xbl/orbeon/date-picker/date-picker-unittest"];
  pages = (function() {
    var _i, _len, _results;
    _results = [];
    for (_i = 0, _len = pages.length; _i < _len; _i++) {
      page = pages[_i];
      _results.push(page + "?orbeon-theme=plain");
    }
    return _results;
  })();
  TestManager.setPages(pages);
  ORBEON.xforms.Events.orbeonLoadedEvent.subscribe(function() {
    TestManager.subscribe(TestManager.TEST_PAGE_BEGIN_EVENT, function(data) {
      return ORBEON.xforms.Document.setValue("page", data);
    });
    TestManager.subscribe(TestManager.TEST_PAGE_COMPLETE_EVENT, function(data) {
      ORBEON.xforms.Document.setValue("report-text", YAHOO.tool.TestFormat.XML(data.results));
      ORBEON.xforms.Document.setValue("page", data.page);
      return ORBEON.xforms.Document.dispatchEvent("main-model", "page-complete");
    });
    TestManager.subscribe(TestManager.TEST_MANAGER_COMPLETE_EVENT, function(data) {
      return ORBEON.xforms.Document.setValue("in-progress", "false");
    });
    return TestManager.start();
  });
}).call(this);
