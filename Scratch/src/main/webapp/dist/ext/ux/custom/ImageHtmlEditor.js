Ext.define('Ext.ux.custom.ImageHtmlEditor', {
	extend : 'Ext.util.Observable',
	alias : 'widget.imagehtmleditor',
	langTitle : '插入图片',
	langIconCls : 'image',
	init : function(view) {
		var scope = this;
		view.on('render', function() {
			scope.onRender(view);
		});
	},

	/**
	 * 添加"插入图片"按钮
	 */
	onRender : function(view) {
		var scope = this;
//		view.getToolbar().add({
//			iconCls : scope.langIconCls,
//			tooltip : {
//				title : scope.langTitle,
//				width : 160,
//				text : '上传本地图片或链接网络图片'
//			},
//			handler : function() {
//				scope.showImgWindow(view);
//			}
//		});
		view.getToolbar().insert(1, {
			iconCls : scope.langIconCls,
			tooltip : {
				title : scope.langTitle,
				width : 160,
				text : '上传本地图片或媒体图片'
			},
			handler : function() {
				scope.showImgWindow(view);
			}
		});
	},

	/**
	 * 显示"插入图片"窗体
	 */
	showImgWindow : function(view) {
		var scope = this;
		var imgUrl;
		var uploadForm = Ext.create('Ext.form.FormPanel', {
//			id : 'uploadForm',
			name : 'uploadForm',
			defaultType : 'textfield',
			labelAlign : 'right',
			labelWidth : 99,
			frame : false,
			fileUpload : true,
			border: false,
			bodyPadding: '5 5 0',
			items : [{
				xtype:'filefield',fieldLabel:'请选择文件',buttonText:'浏览...',labelAlign:'right',
				name:'upload',allowBlank:false,anchor:'99%',
				listeners : {
					change : function(view, value, eOpts) {
						scope.uploadImgCheck(view, value);
					}
				}
			}]
		});
	    
	    var uploadWindow = Ext.create('Ext.Window', {
			title : '上传文件',
			layout : 'fit',
			width : 380,
			height : 120,
			resizable : false,
			draggable : true,
//			closeAction : 'hide',
			title : '上传文件',
			modal : false,
			collapsible : true,
			titleCollapse : true,
			maximizable : false,
			buttonAlign : 'right',
			border : false,
			animCollapse : true,
			constrain : true,
			items : [uploadForm],
			buttons : [{
				text : '上 传',
				iconCls : 'upload',
				handler : function() {
//					var file = Ext.getCmp('uploadFile').getValue();
//					if (Ext.isEmpty(file)) {
//						Ext.Msg.alert('提示', '请先选择您要上传的文件...');
//						return;
//					}
					uploadForm.form.submit({
						url : '/sys/upload.atc',
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
							Ext.example.msg('提示', '上传成功！');
							uploadForm.form.reset();
							uploadWindow.hide();
							store.loadPage(1);
						},
						failure : function(form, action) {
							var msg = action.result.msg;
							Ext.MessageBox.alert('提示', '上传失败:' + msg);
							uploadForm.form.reset();
						}
					});
				}
			}, {	// 窗口底部按钮配置
					text : '重 置', // 按钮文本
					iconCls : 'arrow_rotate_anticlockwise', // 按钮图标
					handler : function() { // 按钮响应函数
						uploadForm.form.reset();
					}
			}]
		});
		var toolbar = Ext.create('Ext.toolbar.Toolbar', {
			region : 'north',
			border : true,
	        items: [{
	        	xtype: 'button',
	        	text: '上传',
	        	iconCls: 'add', 
	        	handler: function(){
	        		uploadWindow.show();
	            }
	        },{
	        	xtype: 'button',
	        	text: '查询',
	        	iconCls: 'preview', 
	        	handler: function(){
	        		searchWindow.show();
	            }
	        },{
	        	xtype: 'button',
	        	text: '选择',
	        	iconCls: 'accept', 
	        	handler: function(){
	        		choosePicture();
	            }
	        }]
	    });
		var store = Ext.create('Ext.data.Store', {
			pageSize : 20,
			autoLoad : true,
	        fields: ['imgName', 'imgPath'],
	        proxy: {
	            type: 'ajax',
	            url : '/sys/querySysImagePage.atc',
	            reader : {
	                root: 'imageList',
	                totalProperty: 'totalCount'
	            }
	        }
	    });
		// 每页显示条数下拉选择框
		var pagesizeCombo = Ext.create('Ext.form.ComboBox', {
			name : 'pagesize',
			triggerAction : 'all',
			mode : 'local',
			store : new Ext.data.ArrayStore({
				fields : [ 'value', 'text' ],
				data : [ [ 10, '10条/页' ], [ 20, '20条/页' ], [ 50, '50条/页' ],
						[ 100, '100条/页' ], [ 200, '200条/页' ], [ 500, '500条/页' ], [ 1000000, '更多条' ] ]
			}),
			valueField : 'value',
			displayField : 'text',
			value : '20',
			editable : false,
			width : 85
		});

		var number = parseInt(pagesizeCombo.getValue());

		// 改变每页显示条数reload数据
		pagesizeCombo.on("select", function(comboBox) {
			bbar.pageSize = parseInt(comboBox.getValue());
			number = parseInt(comboBox.getValue());
			store.pageSize = parseInt(comboBox.getValue());
			store.reload({
				params : {
					start : 0,
					limit : bbar.pageSize
				}
			});
		});

		// 分页工具栏
		var bbar = Ext.create('Ext.PagingToolbar', {
			region: 'south',
			pageSize : number,
			store : store,
			displayInfo : true,
			displayMsg : '显示{0}条到{1}条,共{2}条',
			emptyMsg : "没有符合条件的记录",
			items : [ '-', '&nbsp;&nbsp;', pagesizeCombo ]
		});
		
		var chooseAction = Ext.create('Ext.Action', {
	        iconCls: 'accept',
	        text: '选择',
	        handler: function(widget, event) {
	        	scope.insertImg(view, imgUrl);
	        }
	    });

	    var contextMenu = Ext.create('Ext.menu.Menu', {
	        items: [chooseAction]
	    });
		var panel = Ext.create('Ext.Panel', {
	        id: 'images-view',
	        frame: false,
			autoScroll : true,
			region : 'center',
			border : false,
	        items: Ext.create('Ext.view.View', {
	        	id: 'imageView',
	            store: store,
	            tpl: [
	                '<tpl for=".">',
	                    '<div class="thumb-wrap" id="{name}">',
	                    '<div class="thumb qtip-target">',
	                    '<img src="{imgPath}" data-qtip="{imgName}" /></a></div>',
	                    '<span class="x-editable">{imgName}</span>',
	                    '</div>',
	                '</tpl>'
	            ],
	            //width: bodyWidth - 10,
	            //一定要有一个高度，否则trackOver失效
	            autoScroll: true,
	            height: '100%',
	            autoHeight: true,
//	            multiSelect: true,
//	            trackOver: true,
	            overItemCls: 'x-item-over',
	            itemSelector: 'div.thumb-wrap',
	            emptyText: '<div style="padding:10px;width:100%;text-align:center">没有所需图片！</div>',
	            plugins: [
	                Ext.create('Ext.ux.DataView.DragSelector', {}),
	                Ext.create('Ext.ux.DataView.LabelEditor', {dataIndex: 'name' + 'suffix'})
	            ],
//	            prepareData: function(data) {
//	                Ext.apply(data, {
//	                	dateString: Ext.util.Format.substr(data.datetime, 0, 19)
//	                });
//	                return data;
//	            },
	            listeners: {
	            	//鼠标选中监听，把选中的id放入数组
	                selectionchange: function(panel, item){
	                	if(item.length > 0){
	                		imgUrl = item[0].data.imgPath;
	                	}
	                },
	                itemdblclick: function (panel, record, item, index, e){
	                	imgUrl = record.data.imgPath;
	                	e.stopEvent();
	                	scope.insertImg(view, imgUrl);
	                },
	                itemcontextmenu: function(panel, record, item, index, e) {
	                	imgUrl = record.data.imgPath;
	                    e.stopEvent();
	                    contextMenu.showAt(e.getXY());
	                    return false;
	                }
	            }
	        })
	    });
		Ext.create('Ext.window.Window', {
			id: 'instWin',
			width : 750,
			height : 500,
			title : scope.langTitle,
			layout : 'border',
			autoShow : true,
			modal : true,
			resizable : false,
			maximizable : false,
			constrain : true,
			plain : true,
			enableTabScroll : true,
			border : false,
			items:[toolbar, panel, bbar]
		});
		
	},
	
//	showImgWindow : function(view) {
//		var scope = this;
//		Ext.create('Ext.window.Window', {
//			width : 400,
//			height : 310,
//			title : scope.langTitle,
//			layout : 'fit',
//			autoShow : true,
//			modal : true,
//			resizable : false,
//			maximizable : false,
//			constrain : true,
//			plain : true,
//			enableTabScroll : true,
//			border : false,
//			items : [ {
//				xtype : 'tabpanel',
//				enableTabScroll : true,
//				bodyPadding : 10,
//				items : [ {
//					title : '上传本地图片',
//					items : [ {
//						xtype : 'form',
//						layout : 'column',
//						autoScroll : true,
//						border : false,
//						defaults : {
//							columnWidth : 1,
//							labelWidth : 80,
//							labelAlign : 'left',
//							padding : 5,
//							allowBlank : false
//						},
//						items : [ {
//							xtype : 'fileuploadfield',
//							fieldLabel : '选择文件',
//							afterLabelTextTpl : '<span style="color:#FF0000;">*</span>',
//							buttonText : '请选择...',
//							name : 'sysImage.upload',
//							emptyText : '请选择图片',
//							blankText : '图片不能为空',
//							listeners : {
//								change : function(view, value, eOpts) {
//									scope.uploadImgCheck(view, value);
//								}
//							}
//						}, {
//							xtype : 'fieldcontainer',
//							fieldLabel : '图片大小',
//							layout : 'hbox',
//							defaultType : 'numberfield',
//							defaults : {
//								flex : 1,
//								labelWidth : 20,
//								labelAlign : 'left',
//								allowBlank : true
//							},
//							items : [ {
//								fieldLabel : '宽',
//								name : 'width',
//								minValue : 1
//							}, {
//								fieldLabel : '高',
//								name : 'height',
//								minValue : 1
//							} ]
//						}, {
//							xtype : 'textfield',
//							fieldLabel : '图片说明',
//							name : 'content',
//							allowBlank : true,
//							maxLength : 100,
//							emptyText : '简短的图片说明'
//						}, {
//							columnWidth : 1,
//							xtype : 'fieldset',
//							title : '上传须知',
//							layout : {
//								type : 'table',
//								columns : 1
//							},
//							collapsible : false,// 是否可折叠
//							defaultType : 'label',// 默认的Form表单组件
//							items : [ {
//								html : '1、上传图片大小不超过2MB.'
//							}, {
//								html : '2、支持以下格式的图片:jpg,jpeg,png,gif,bmp.'
//							} ]
//						} ],
//						buttons : [ '->', {
//							text : '保存',
//							action : 'btn_save',
//							iconCls : 'icon-save',
//							handler : function(btn) {
//								scope.saveUploadImg(btn, view);
//							}
//						}, {
//							text : '取消',
//							iconCls : 'icon-cancel',
//							handler : function(btn) {
//								btn.up('window').close();
//							}
//						}, '->' ]
//					} ]
//				}, {
//					title : '链接网络图片',
//					items : [ {
//						xtype : 'form',
//						layout : 'column',
//						autoScroll : true,
//						border : false,
//						defaults : {
//							columnWidth : 1,
//							labelWidth : 80,
//							labelAlign : 'left',
//							padding : 5,
//							allowBlank : false
//						},
//						items : [{
//							xtype : 'textfield',
//							fieldLabel : '图片地址',
//							afterLabelTextTpl : '<span style="color:#FF0000;">*</span>',
//							name : 'url',
//							emptyText : '请填入支持外链的长期有效的图片URL',
//							blankText : '图片地址不能为空',
//							vtype : 'url'
//						}, {
//							xtype : 'fieldcontainer',
//							fieldLabel : '图片大小',
//							layout : 'hbox',
//							defaultType : 'numberfield',
//							defaults : {
//								flex : 1,
//								labelWidth : 20,
//								labelAlign : 'left',
//								allowBlank : true
//							},
//							items : [ {
//								fieldLabel : '宽',
//								name : 'width',
//								minValue : 1
//							}, {
//								fieldLabel : '高',
//								name : 'height',
//								minValue : 1
//							} ]
//						}, {
//							xtype : 'textfield',
//							fieldLabel : '图片说明',
//							name : 'content',
//							allowBlank : true,
//							maxLength : 100,
//							emptyText : '简短的图片说明'
//						} ],
//						buttons : [ '->', {
//							text : '保存',
//							action : 'btn_save',
//							iconCls : 'icon-save',
//							handler : function(btn) {
//								scope.saveRemoteImg(btn, view);
//							}
//						}, {
//							text : '取消',
//							iconCls : 'icon-cancel',
//							handler : function(btn) {
//								btn.up('window').close();
//							}
//						}, '->' ]
//					} ]
//				} ]
//			} ]
//		});
//	},

	/**
	 * 上传图片验证
	 */
	uploadImgCheck : function(fileObj, fileName) {
		var scope = this;
		// 图片类型验证
		if (!(scope.getImgTypeCheck(scope.getImgHZ(fileName)))) {
			Ext.MessageBox.show({
	           title: '提示',
	           msg: "上传图片类型有误！",
	           buttons: Ext.MessageBox.OK,
	           icon: Ext.MessageBox.ERROR
            });
			fileObj.reset();// 清空上传内容
			return;
		}
	},

	/**
	 * 获取图片后缀(小写)
	 */
	getImgHZ : function(imgName) {
		// 后缀
		var hz = '';
		// 图片名称中最后一个.的位置
		var index = imgName.lastIndexOf('.');
		if (index != -1) {
			// 后缀转成小写
			hz = imgName.substr(index + 1).toLowerCase();
		}
		return hz;
	},

	/**
	 * 图片类型验证
	 */
	getImgTypeCheck : function(hz) {
		var typestr = 'jpg,jpeg,png,gif,bmp';
		var types = typestr.split(',');// 图片类型
		for (var i = 0; i < types.length; i++) {
			if (hz == types[i]) {
				return true;
			}
		}
		return false;
	},

	/**
	 * 插入图片
	 */
	insertImg : function(view, data) {
		var url = data;
//		var content = data.content;
//		var width = data.width;
//		var height = data.height;
		var str = '<img src="' + url + '" border="0" ';
//		if (content != undefined && content != null && content != '') {
//			str += ' title="' + content + '" ';
//		}
//		if (width != undefined && width != null && width != 0) {
//			str += ' width="' + width + '" ';
//		}
//		if (height != undefined && height != null && height != 0) {
//			str += ' height="' + height + '" ';
//		}
		str += ' />';
		view.insertAtCursor(str);
		Ext.getCmp("instWin").close();
	}
});
