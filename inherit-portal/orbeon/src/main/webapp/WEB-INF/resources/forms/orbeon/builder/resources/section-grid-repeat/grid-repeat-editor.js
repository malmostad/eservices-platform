(function() {
  $(function() {
    var AjaxServer, Builder, Events, gridsCache;
    AjaxServer = ORBEON.xforms.server.AjaxServer;
    Builder = ORBEON.Builder;
    Events = ORBEON.xforms.Events;
    gridsCache = [];
    (function() {
      return Builder.onOffsetMayHaveChanged(function() {
        gridsCache.length = 0;
        return _.each($('.fr-grid.fr-editable:visible'), function(table) {
          var grid, gridInfo, head;
          table = $(table);
          grid = table.parent();
          gridInfo = {
            el: grid,
            offset: Builder.adjustedOffset(grid),
            height: f$.outerHeight(grid)
          };
          if (f$.is('.fr-repeat', table)) {
            head = f$.find('thead', table);
            gridInfo.head = {
              offset: Builder.adjustedOffset(head),
              height: f$.height(head)
            };
          }
          gridInfo.rows = _.map(f$.find('.fb-grid-tr', grid), function(tr) {
            return {
              grid: gridInfo,
              el: $(tr),
              offset: Builder.adjustedOffset($(tr)),
              height: f$.height($(tr))
            };
          });
          gridInfo.cols = _.map(f$.find('.fb-grid-tr:first .fb-grid-td', grid), function(td) {
            return {
              grid: gridInfo,
              el: $(td),
              offset: Builder.adjustedOffset($(td)),
              width: f$.width($(td))
            };
          });
          return gridsCache.unshift(gridInfo);
        });
      });
    })();
    (function() {
      var deleteIcon, detailsHeight, detailsIcon;
      deleteIcon = $('.fb-delete-grid-trigger');
      detailsIcon = $('.fb-grid-details-trigger');
      detailsHeight = _.memoize(function() {
        return f$.height(detailsIcon);
      });
      return Builder.currentContainerChanged(gridsCache, {
        wasCurrent: function() {
          return _.each([deleteIcon, detailsIcon], function(i) {
            return f$.hide(i);
          });
        },
        becomesCurrent: function(grid) {
          var head, offset, table;
          table = f$.children('.fr-grid', grid.el);
          if (f$.is('.fb-can-delete-grid', table)) {
            f$.show(deleteIcon);
            offset = {
              top: grid.offset.top - Builder.scrollTop(),
              left: grid.offset.left
            };
            f$.offset(offset, deleteIcon);
          }
          if (grid.head != null) {
            f$.show(detailsIcon);
            head = f$.find('thead', table);
            offset = {
              top: grid.head.offset.top + (grid.head.height - detailsHeight()) / 2 - Builder.scrollTop(),
              left: grid.offset.left
            };
            return f$.offset(offset, detailsIcon);
          }
        }
      });
    })();
    (function() {
      var colIcons, hideIcons, icon, rowIcons, showIcons;
      icon = function(selector) {
        return _.tap({}, function(icon) {
          icon.el = $(selector);
          icon.width = _.memoize(function() {
            return f$.width(icon.el);
          });
          return icon.height = _.memoize(function() {
            return f$.height(icon.el);
          });
        });
      };
      colIcons = (function() {
        var colIcon;
        colIcon = function(selector, colOffset) {
          return _.tap(icon(selector), function(icon) {
            return icon.offset = function(col) {
              return {
                top: col.grid.offset.top - Builder.scrollTop(),
                left: col.offset.left + colOffset(col, icon)
              };
            };
          });
        };
        return [
          colIcon('.fb-insert-col-left', function() {
            return 0;
          }), colIcon('.fb-delete-col', function(col, icon) {
            return (col.width - icon.width()) / 2;
          }), colIcon('.fb-insert-col-right', function(col, icon) {
            return col.width - icon.width();
          })
        ];
      })();
      rowIcons = (function() {
        var rowIcon;
        rowIcon = function(selector, rowOffset) {
          return _.tap(icon(selector), function(icon) {
            return icon.offset = function(row) {
              return {
                top: row.offset.top + (rowOffset(row, icon)) - Builder.scrollTop(),
                left: row.grid.offset.left
              };
            };
          });
        };
        return [
          rowIcon('.fb-insert-row-above', function() {
            return 0;
          }), rowIcon('.fb-delete-row', function(row, icon) {
            return (row.height - icon.height()) / 2;
          }), rowIcon('.fb-insert-row-below', function(row, icon) {
            return row.height - icon.height();
          })
        ];
      })();
      hideIcons = function(icons) {
        return function() {
          return _.each(_.values(icons), function(icon) {
            return f$.hide(icon.el);
          });
        };
      };
      showIcons = function(icons) {
        return function(rowOrCol) {
          return _.each(icons, function(icon) {
            var canDo, dontShow, operationRequires;
            canDo = function(operation) {
              var gridDiv, gridTable;
              gridDiv = rowOrCol.grid.el;
              gridTable = f$.children('.fr-grid', gridDiv);
              return f$.is('.fb-can-' + operation, gridTable);
            };
            operationRequires = {
              'delete-row': 'delete-row',
              'delete-col': 'delete-col',
              'insert-col-left': 'add-col',
              'insert-col-right': 'add-col'
            };
            dontShow = _.any(_.keys(operationRequires), function(operation) {
              return (f$.is('.fb-' + operation, icon.el)) && (!canDo(operationRequires[operation]));
            });
            if (!dontShow) {
              f$.show(icon.el);
              return f$.offset(icon.offset(rowOrCol), icon.el);
            }
          });
        };
      };
      return Builder.currentRowColChanged(gridsCache, {
        wasCurrentRow: hideIcons(rowIcons),
        becomesCurrentRow: showIcons(rowIcons),
        wasCurrentCol: hideIcons(colIcons),
        becomesCurrentCol: showIcons(colIcons)
      });
    })();
    return (function() {
      var current, resetPos, setPos;
      current = {
        gridId: null,
        colPos: -1,
        rowPos: -1
      };
      resetPos = function(pos) {
        return function() {
          return current[pos] = -1;
        };
      };
      setPos = function(pos) {
        return function(rowCol) {
          var selector;
          selector = '.fb-grid-' + rowCol.el[0].nodeName.toLowerCase();
          return current[pos] = f$.length(f$.prevAll(selector, rowCol.el));
        };
      };
      Builder.currentRowColChanged(gridsCache, {
        wasCurrentRow: resetPos('rowPos'),
        becomesCurrentRow: setPos('rowPos'),
        wasCurrentCol: resetPos('colPos'),
        becomesCurrentCol: setPos('colPos')
      });
      Builder.currentContainerChanged(gridsCache, {
        wasCurrent: function() {
          return current.gridId = null;
        },
        becomesCurrent: function(grid) {
          return current.gridId = f$.attr('id', grid.el);
        }
      });
      return AjaxServer.eventCreated.add(function(event) {
        var add, classContains, inGridRepeatEditor, target;
        target = $(document.getElementById(event.targetId));
        inGridRepeatEditor = f$.is('*', f$.closest('.fb-grid-repeat-editor', target));
        if (event.eventName === 'DOMActivate' && inGridRepeatEditor) {
          classContains = function(text) {
            return f$.is('*[class *= "' + text + '"]', target);
          };
          add = function(name, value) {
            return event.properties[name] = value.toString();
          };
          add('grid-id', current.gridId);
          if (classContains('row')) {
            add('row-pos', current.rowPos);
          }
          if (classContains('col')) {
            return add('col-pos', current.colPos);
          }
        }
      });
    })();
  });
}).call(this);
