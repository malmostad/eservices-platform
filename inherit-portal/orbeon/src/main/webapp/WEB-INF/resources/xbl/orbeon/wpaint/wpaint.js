(function() {
  var OD, YD;
  var __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; };
  OD = ORBEON.xforms.Document;
  YD = YAHOO.util.Dom;
  YAHOO.namespace('xbl.fr');
  YAHOO.xbl.fr.WPaint = function() {};
  ORBEON.xforms.XBL.declareClass(YAHOO.xbl.fr.WPaint, "xbl-fr-wpaint");
  YAHOO.xbl.fr.WPaint.prototype = {
    init: function() {
      var testCanvasEl;
      this.annotationEl = $(this.container).find('.fr-wpaint-annotation');
      this.imageEl = $(this.container).find('.fr-wpaint-image img');
      this.wpaintElA = $(this.container).find('.fr-wpaint-container-a');
      this.wpaintElB = $(this.container).find('.fr-wpaint-container-b');
      this.wpaintElC = null;
      testCanvasEl = document.createElement('canvas');
      this.canvasSupported = !!(testCanvasEl.getContext && testCanvasEl.getContext('2d'));
      if (this.canvasSupported) {
        $(this.container).find('.fr-wpaint-no-canvas').addClass('xforms-hidden');
        $(this.container).find('.xforms-upload').removeClass('xforms-hidden');
        return this.imageEl.imagesLoaded(__bind(function() {
          return this.imageLoaded();
        }, this));
      }
    },
    enabled: function() {},
    imageLoaded: function() {
      var annotation, imageIsEmpty, imageSrc;
      if (this.canvasSupported) {
        imageSrc = this.imageEl.attr('src');
        imageIsEmpty = !_.isNull(imageSrc.match(/spacer.gif$/));
        if (imageIsEmpty) {
          this.wpaintElA.addClass('xforms-hidden');
          if (!_.isNull(this.wpaintElC)) {
            this.wpaintElC.detach();
            this.wpaintElC = null;
          }
        } else {
          this.wpaintElA.removeClass('xforms-hidden');
          this.wpaintElA.css('width', this.imageEl.width() + 'px');
          this.wpaintElB.css('padding-top', (this.imageEl.height() / this.imageEl.width() * 100) + '%');
          this.wpaintElC = $('<div class="fr-wpaint-container-c" tabindex="-1"/>');
          this.wpaintElB.append(this.wpaintElC);
          this.wpaintElC.blur(__bind(function() {
            return this.blur();
          }, this));
          this.wpaintElC.css('width', this.imageEl.width() + 'px');
          this.wpaintElC.css('height', this.imageEl.height() + 'px');
          annotation = this.annotationEl.attr('src');
          this.wpaintElC.wPaint({
            drawDown: __bind(function() {
              return this.drawDown();
            }, this),
            imageBg: this.imageEl.attr('src'),
            image: annotation === "" ? null : annotation
          });
        }
        return this.imageEl.one('load', __bind(function() {
          return this.imageLoaded();
        }, this));
      }
    },
    drawDown: function() {
      return this.wpaintElC.focus();
    },
    blur: function() {
      var annotationImgData;
      annotationImgData = this.wpaintElC.wPaint('image');
      return OD.dispatchEvent({
        targetId: this.container.id,
        eventName: 'fr-update-annotation',
        properties: {
          value: annotationImgData
        }
      });
    },
    readonly: function() {},
    readwrite: function() {}
  };
}).call(this);
