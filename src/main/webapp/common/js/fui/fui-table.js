define(['bootstrapTableLocale'], function(){
	// 重写bootstraptable footer方法
	var BootstrapTable = $.fn.bootstrapTable.Constructor
		,sprintf = $.fn.bootstrapTable.utils.sprintf
		,calculateObjectValue = $.fn.bootstrapTable.utils.calculateObjectValue
		,oldLoadFn = BootstrapTable.prototype.load;
	BootstrapTable.prototype.load = function(res) {
		this["aggsData"] = res.aggsData;
		oldLoadFn.call(this, res);
	}
	var renderRow = function(cs, fn, that) {
		var data = that.getData()
			,html = [];
		html.push("<tr>");
		$.each(cs, function(i, column) {
			if (!column.visible) {
				return;
			}
			if (that.options.cardView && (!column.cardVisible)) {
				return;
			}
			var falign = ''// footer align style
				,style = ''
				,class_ = sprintf(' class="%s"', column['class'])
				,widthStr = "";
			falign = sprintf('text-align: %s; ', column.falign ? column.falign : column.align);
			style = sprintf('vertical-align: %s; ', column.valign);
			if (column.width != undefined) {
				widthStr = " width=" + column.width + " ";
			}
			html.push('<td', widthStr, class_, sprintf(' style="%s"', falign + style), '>');
			html.push('<div class="th-inner">');
			html.push(calculateObjectValue(column, column[fn], [ data, that.aggsData ], '&nbsp;') || '&nbsp;');
			html.push('</div>');
			html.push('<div class="fht-cell"></div>');
			html.push('</div>');
			html.push('</td>');
		});
		html.push("</tr>");
		return html;
	};
	var resetFooterFn = BootstrapTable.prototype.resetFooter;

	BootstrapTable.prototype.resetFooter = function() {
		if(this.getData().length==0){
			this.$tableFooter.hide();
			return;
		}
		this.$tableFooter.show();
		if (!this.options.showPageSummary && !this.options.showAllSummary) {
			resetFooterFn.call(this);
			return;
		}
		var that = this;

		if (!this.options.showFooter || this.options.cardView) { // do
			return;
		}
		
		if (!this.options.cardView && this.options.detailView) {
			html.push('<td><div class="th-inner">&nbsp;</div><div class="fht-cell"></div></td>');
		}
		var html = "";
		if (this.options.showPageSummary) {
			var h1 = renderRow(this.columns, "pageSummaryFormat", that);
			html += h1.join('');
		}
		if (this.options.showAllSummary) {
			var h2 = renderRow(this.columns, "allSummaryFormat", that);
			html += h2.join('');
		}
		this.$tableFooter.html("<table><tbody>" + html + "</tbody></table>");
		
		clearTimeout(this.timeoutFooter_);
		this.timeoutFooter_ = setTimeout($.proxy(this.fitFooter, this),
			this.$el.is(':hidden') ? 100 : 0);
	};
	
	BootstrapTable.prototype.onSort = function(event) {
		var $this = event.type === "keypress" ? $(event.currentTarget) : $(event.currentTarget).parent(), $this_ = this.$header.find('th').eq($this.index());
		this.$header.add(this.$header_).find('span.order').remove();
	
		if (this.options.sortName === $this.data('field')) {
			this.options.sortOrder = this.options.sortOrder === 'asc' ? 'desc' : 'asc';
		} else {
			this.options.sortName = $this.data('field');
			this.options.sortOrder = $this.data('order') === 'asc' ? 'desc' : 'asc';
		}
		this.trigger('sort', this.options.sortName, this.options.sortOrder);
	
		$this.add($this_).data('order', this.options.sortOrder);
	
		this.getCaret();
	
		if (this.options.sidePagination === 'server' && this.options.sortType != 'local') {
			this.initServer(this.options.silentSort);
			return;
		}
		
		this.initSort();
		this.initBody();
	};
	BootstrapTable.prototype.initPagination = function () {
        if (!this.options.pagination) {
            this.$pagination.hide();
            return;
        } else {
            this.$pagination.show();
        }

        var that = this,
            html = [],
            $allSelected = false,
            i, from, to,
            $pageList,
            $first, $pre,
            $next, $last,
            $number,
            data = this.getData(),
            pageList = this.options.pageList;

        if (this.options.sidePagination !== 'server') {
            this.options.totalRows = data.length;
        }

        this.totalPages = 0;
        if (this.options.totalRows) {
            if (this.options.pageSize === this.options.formatAllRows()) {
                this.options.pageSize = this.options.totalRows;
                $allSelected = true;
            } else if (this.options.pageSize === this.options.totalRows) {
                // Fix #667 Table with pagination,
                // multiple pages and a search that matches to one page throws exception
                var pageLst = typeof this.options.pageList === 'string' ?
                    this.options.pageList.replace('[', '').replace(']', '')
                        .replace(/ /g, '').toLowerCase().split(',') : this.options.pageList;
                if ($.inArray(this.options.formatAllRows().toLowerCase(), pageLst)  > -1) {
                    $allSelected = true;
                }
            }

            this.totalPages = ~~((this.options.totalRows - 1) / this.options.pageSize) + 1;

            this.options.totalPages = this.totalPages;
        }
        if (this.totalPages > 0 && this.options.pageNumber > this.totalPages) {
            this.options.pageNumber = this.totalPages;
        }

        this.pageFrom = (this.options.pageNumber - 1) * this.options.pageSize + 1;
        this.pageTo = this.options.pageNumber * this.options.pageSize;
        if (this.pageTo > this.options.totalRows) {
            this.pageTo = this.options.totalRows;
        }

        html.push(
            '<div class="pull-' + this.options.paginationDetailHAlign + ' pagination-detail">',
            '<span class="pagination-info">',
            this.options.onlyInfoPagination ? this.options.formatDetailPagination(this.options.totalRows) :
            this.options.formatShowingRows(this.pageFrom, this.pageTo, this.options.totalRows),
            '</span>');

        if (!this.options.onlyInfoPagination) {
            html.push('<span class="page-list">');

            var pageNumber = [
                    sprintf('<span class="btn-group %s">',
                        this.options.paginationVAlign === 'top' || this.options.paginationVAlign === 'both' ?
                            'dropdown' : 'dropup'),
                    '<button type="button" class="btn' +
                    sprintf(' btn-%s', this.options.buttonsClass) +
                    sprintf(' btn-%s', this.options.iconSize) +
                    ' dropdown-toggle" data-toggle="dropdown">',
                    '<span class="page-size">',
                    $allSelected ? this.options.formatAllRows() : this.options.pageSize,
                    '</span>',
                    ' <span class="caret"></span>',
                    '</button>',
                    '<ul class="dropdown-menu" role="menu">'
                ];

            if (typeof this.options.pageList === 'string') {
                var list = this.options.pageList.replace('[', '').replace(']', '')
                    .replace(/ /g, '').split(',');

                pageList = [];
                $.each(list, function (i, value) {
                    pageList.push(value.toUpperCase() === that.options.formatAllRows().toUpperCase() ?
                        that.options.formatAllRows() : +value);
                });
            }

            $.each(pageList, function (i, page) {
                if (!that.options.smartDisplay || i === 0 || pageList[i - 1] <= that.options.totalRows) {
                    var active;
                    if ($allSelected) {
                        active = page === that.options.formatAllRows() ? ' class="active"' : '';
                    } else {
                        active = page === that.options.pageSize ? ' class="active"' : '';
                    }
                    pageNumber.push(sprintf('<li%s><a href="javascript:void(0)">%s</a></li>', active, page));
                }
            });
            pageNumber.push('</ul></span>');

            html.push(this.options.formatRecordsPerPage(pageNumber.join('')));
            html.push('</span>');

            html.push('</div>',
                '<div class="pull-' + this.options.paginationHAlign + ' pagination">',
                '<ul class="pagination' + sprintf(' pagination-%s', this.options.iconSize) + '">',
                '<li class="page-pre"><a href="javascript:void(0)">' + this.options.paginationPreText + '</a></li>');

            if (this.totalPages < 5) {
                from = 1;
                to = this.totalPages;
            } else {
                from = this.options.pageNumber - 2;
                to = from + 4;
                if (from < 1) {
                    from = 1;
                    to = 5;
                }
                if (to > this.totalPages) {
                    to = this.totalPages;
                    from = to - 4;
                }
            }

            if (this.totalPages >= 6) {
                if (this.options.pageNumber >= 3) {
                    html.push('<li class="page-first' + (1 === this.options.pageNumber ? ' active' : '') + '">',
                        '<a href="javascript:void(0)">', 1, '</a>',
                        '</li>');

                    from++;
                }

                if (this.options.pageNumber >= 4) {
                    if (this.options.pageNumber == 4 || this.totalPages == 6 || this.totalPages == 7) {
                        from--;
                    } else {
                        html.push('<li class="page-first-separator disabled">',
                            '<a href="javascript:void(0)">...</a>',
                            '</li>');
                    }

                    to--;
                }
            }

            if (this.totalPages >= 7) {
                if (this.options.pageNumber >= (this.totalPages - 2)) {
                    from--;
                }
            }

            if (this.totalPages == 6) {
                if (this.options.pageNumber >= (this.totalPages - 2)) {
                    to++;
                }
            } else if (this.totalPages >= 7) {
                if (this.totalPages == 7 || this.options.pageNumber >= (this.totalPages - 3)) {
                    to++;
                }
            }

            for (i = from; i <= to; i++) {
                html.push('<li class="page-number' + (i === this.options.pageNumber ? ' active' : '') + '">',
                    '<a href="javascript:void(0)">', i, '</a>',
                    '</li>');
            }

            if (this.totalPages >= 8) {
                if (this.options.pageNumber <= (this.totalPages - 4)) {
                    html.push('<li class="page-last-separator disabled">',
                        '<a href="javascript:void(0)">...</a>',
                        '</li>');
                }
            }

            if (this.totalPages >= 6) {
                if (this.options.pageNumber <= (this.totalPages - 3)) {
                    html.push('<li class="page-last' + (this.totalPages === this.options.pageNumber ? ' active' : '') + '">',
                        '<a href="javascript:void(0)">', this.totalPages, '</a>',
                        '</li>');
                }
            }

            html.push(
                    '<li class="page-next"><a href="javascript:void(0)">' + this.options.paginationNextText + '</a></li>',
                    '</ul><div class="pull-right"><div class="input-group" style="width:100px;margin-left:5px;">',
                    '<input type="text" class="form-control" placeholder="页码" value="'+this.options.pageNumber+'">',
                    '<span class="input-group-btn"><button class="btn btn-default" type="button">Go</button></span></div></div></div></div>');
        }
        this.$pagination.html(html.join(''));

        if (!this.options.onlyInfoPagination) {
            $pageList = this.$pagination.find('.page-list a');
            $first = this.$pagination.find('.page-first');
            $pre = this.$pagination.find('.page-pre');
            $next = this.$pagination.find('.page-next');
            $last = this.$pagination.find('.page-last');
            $number = this.$pagination.find('.page-number');

            if (this.options.smartDisplay) {
                if (this.totalPages <= 1) {
                    this.$pagination.find('div.pagination').hide();
                }
                if (pageList.length < 2 || this.options.totalRows <= pageList[0]) {
                    this.$pagination.find('span.page-list').hide();
                }

                // when data is empty, hide the pagination
                this.$pagination[this.getData().length ? 'show' : 'hide']();
            }
            if ($allSelected) {
                this.options.pageSize = this.options.formatAllRows();
            }
            $pageList.off('click').on('click', $.proxy(this.onPageListChange, this));
            $first.off('click').on('click', $.proxy(this.onPageFirst, this));
            $pre.off('click').on('click', $.proxy(this.onPagePre, this));
            $next.off('click').on('click', $.proxy(this.onPageNext, this));
            $last.off('click').on('click', $.proxy(this.onPageLast, this));
            $number.off('click').on('click', $.proxy(this.onPageNumber, this));
            this.$pagination.find('.btn').off('click').on('click', $.proxy(function (event) {
            	var p= this.$pagination.find('.form-control').val();
            	if(p && this.options.pageNumber != +p){
            		this.options.pageNumber = +p;
                    this.updatePagination(event);
            	}
            }, this));
        }
    };
	return {
		createBootstrapTable:function(options){
			var op = $.extend({
				method : 'post',
				cache : false,
				striped : true,
				pagination : true,
				pageList : [ 10,25,50,100,200,500],
				contentType : "application/x-www-form-urlencoded",
				pageSize : 20,
				pageNumber : 1,
				queryParamsType : null,
				sidePagination : 'server',// 设置为服务器端分页
				showRefresh : true,
				showColumns : true,
				showToggle : false,
				minimumCountColumns : 2
			},options);
			if(op.form){
				var $form=op.form,oldQueryParams=op.queryParams;
				op.queryParams=function(params){
					if(oldQueryParams){
						params=oldQueryParams(params,$form);
					}
					var paramArr=$form.serializeArray()
					$.each( paramArr, function(i, field){
						params[field.name]=field.value;
					});
					return params;
				}
			}
			var oldLoadSuccess=op.onLoadSuccess;
			op.onLoadSuccess=function(r){
				if(!r || r.success==false){
					layer.msg(r.msg||"网络出错，清刷新重试");
					return;
				}
				if(oldLoadSuccess){
					oldLoadSuccess(op.table,r);
				}
				Fui.initUI(op.table);
			}
			var r = op.table.bootstrapTable(op);
			if(op.onCreatedSuccessFun){
				op.onCreatedSuccessFun(r);
			}
			return r;
		}
	}
});
