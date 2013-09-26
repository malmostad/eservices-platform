(function() {
  var Condition, Controls, Event, Events, InViewportCondition, OnScreenCondition, Page, Property, YD, initInViewPortProperty;
  var __hasProp = Object.prototype.hasOwnProperty, __extends = function(child, parent) {
    for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; }
    function ctor() { this.constructor = child; }
    ctor.prototype = parent.prototype;
    child.prototype = new ctor;
    child.__super__ = parent.prototype;
    return child;
  }, __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; };
  YD = YAHOO.util.Dom;
  Event = YAHOO.util.Event;
  Events = ORBEON.xforms.Events;
  Property = ORBEON.util.Property;
  Page = ORBEON.xforms.Page;
  Controls = ORBEON.xforms.Controls;
  initInViewPortProperty = new Property("xbl.fr.datatable.init-in-viewport", false);
  Condition = (function() {
    function Condition(container) {
      this.container = container;
    }
    return Condition;
  })();
  InViewportCondition = (function() {
    function InViewportCondition() {
      InViewportCondition.__super__.constructor.apply(this, arguments);
    }
    __extends(InViewportCondition, Condition);
    InViewportCondition.prototype.isMet = function() {
      var containerBottom, containerTop, viewportBottom, viewportTop;
      containerTop = YD.getY(this.container);
      containerBottom = containerTop + this.container.offsetHeight;
      viewportTop = YD.getDocumentScrollTop();
      viewportBottom = viewportTop + YD.getViewportHeight();
      return containerTop < viewportBottom && viewportTop < containerBottom;
    };
    InViewportCondition.prototype.addListener = function(listener) {
      return Event.addListener(window, "scroll", listener);
    };
    InViewportCondition.prototype.removeListener = function(listener) {
      return Event.removeListener(window, "scroll", listener);
    };
    return InViewportCondition;
  })();
  OnScreenCondition = (function() {
    function OnScreenCondition() {
      OnScreenCondition.__super__.constructor.apply(this, arguments);
    }
    __extends(OnScreenCondition, Condition);
    OnScreenCondition.prototype.isMet = function() {
      return _.all(["xforms-case-deselected", "xforms-initially-hidden"], __bind(function(name) {
        var firstChild;
        firstChild = YD.getFirstChild(this.container);
        return (YD.getAncestorByClassName(firstChild, name)) === null;
      }, this));
    };
    OnScreenCondition.prototype.addListener = function(listener) {
      return Events.ajaxResponseProcessedEvent.subscribe(listener);
    };
    OnScreenCondition.prototype.removeListener = function(listener) {
      return Events.ajaxResponseProcessedEvent.unsubscribe(listener);
    };
    return OnScreenCondition;
  })();
  YAHOO.xbl.fr.Datatable.prototype.getConditions = function() {
    return _.compact([new OnScreenCondition(this.container), initInViewPortProperty.get() ? new InViewportCondition(this.container) : void 0]);
  };
  YAHOO.xbl.fr.Datatable.prototype.isDisplayed = function() {
    return _.all(this.getConditions(), __bind(function(condition) {
      return condition.isMet(this.container);
    }, this));
  };
  YAHOO.xbl.fr.Datatable.prototype.whenDisplayed = function(ready) {
    var onConditions;
    onConditions = __bind(function(conditions, conditionsMet) {
      var formElement, listener, orbeonForm, unmetCondition;
      unmetCondition = _.detect(conditions, function(condition) {
        return !condition.isMet();
      });
      if (unmetCondition != null) {
        listener = function() {
          if (unmetCondition.isMet()) {
            unmetCondition.removeListener(listener);
            return onConditions(conditions, conditionsMet);
          }
        };
        return unmetCondition.addListener(listener);
      } else {
        formElement = Controls.getForm(this.container);
        orbeonForm = Page.getForm(formElement.id);
        return orbeonForm.getLoadingIndicator().runShowing(conditionsMet);
      }
    }, this);
    return onConditions(this.getConditions(), function() {
      return ready();
    });
  };
}).call(this);
