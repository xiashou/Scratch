/*
 * Banner图片管理
 * xs
 * 2018/04/03
 */
Ext.define('Business.BannerManagement', {
	extend: 'Ext.ux.desktop.Module',

    requires: [
        'Ext.util.Format',
        'Ext.grid.Panel',
        'Ext.grid.RowNumberer'
    ],

    id:'banner-mgr',

    init : function(){
        this.launcher = {
            text: 'Banner图片管理',
            iconCls:'banner'
        };
    },

    createWindow : function(){
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('banner-mgr');
        var store = new Ext.data.Store({
    		pageSize : 20,
    		fields: ['id', 'appid', 'bannerUrl', 'linkUrl', 'sortNo'],
    		proxy : {
    			type : 'ajax',
    			url : '/biz/setting/queryBannerList.atc',
    			reader : {
    				root : 'bannerList'
    			}
    		}
    	});
//        var pagesizeCombo = desktop.getPagesizeCombo();
//    	var number = parseInt(pagesizeCombo.getValue());
//    	pagesizeCombo.on("select", function(comboBox) {
//    		bbar.pageSize = parseInt(comboBox.getValue());
//    		number = parseInt(comboBox.getValue());
//    		store.pageSize = parseInt(comboBox.getValue());
//    		store.reload({
//    			params : {
//    				start : 0,
//    				limit : bbar.pageSize
//    			}
//    		});
//    	});
//    	
//    	var bbar = new Ext.PagingToolbar({
//    		pageSize : number,
//    		store : store,
//    		displayInfo : true,
//    		displayMsg : '显示{0}条到{1}条,共{2}条',
//    		emptyMsg : "没有符合条件的记录",
//    		items : [ '-', '&nbsp;&nbsp;', pagesizeCombo ]
//    	});
    	
    	var bannerForm = new Ext.form.FormPanel({
    		id : 'bannerForm',
    		layout : 'anchor',
    		defaults : {
    			anchor : '100%',
    			layout : 'hbox',
    			xtype : 'fieldcontainer'
    		},
    		bodyPadding : '5 10 0 0',
    		border : false,
    		items : [ {
    			defaults : {
    				flex : 1,
    				xtype : 'textfield',
    				labelWidth : 70,
    				labelAlign : 'right'
    			},
    			items : [ {
    				id : 'sortNo',
    				xtype : 'numberfield',
    				name : 'banner.sortNo',
    				fieldLabel : '排序',
    			},{
    				id : 'id',
    				name : 'banner.id',
    				xtype: 'hiddenfield'
    			}]
    		},{
    			defaults : {
    				flex : 1,
    				xtype : 'filefield',
    				labelWidth : 70,
    				labelAlign : 'right'
    			},
    			items : [ {
    				fieldLabel : '图片',
    				id : 'upload',
    				name: 'upload',
    				buttonText: '浏览...',
    				anchor : '99%'
    			} ]
    		},{
    			defaults : {
    				flex : 1,
    				xtype : 'textfield',
    				labelWidth : 70,
    				labelAlign : 'right'
    			},
    			items : [ {
    				fieldLabel : '链接地址',
    				id : 'linkUrl',
    				name: 'banner.linkUrl',
    				anchor : '99%'
    			} ]
    		},{
    			defaults : {
    				flex : 1,
    				xtype : 'displayfield',
    				labelWidth : 70
    			},
    			items: [ {
    				fieldLabel : '&nbsp;',
    				labelSeparator: '',
    				value:'图片最佳尺寸864*470'
    			} ]
    		} ],
    		buttons : [ {
    			text : '保 存',
    			iconCls : 'accept',
    			handler : function() {
    				if (bannerForm.form.isValid()) {
    					bannerForm.form.submit({
    						waitTitle : '提示',
    						method : 'POST',
    						waitMsg : '正在处理数据,请稍候...',
    						success : function(form, action) {
    							bannerWindow.hide();
    							console.log(action.result.msg);
    							desktop.showMessage(action.result.msg);
    							store.reload();
    						},
    						failure : function(form, action) {
    							var msg = action.result.msg;
    							Ext.MessageBox.alert('提示', msg);
    						}
    					});
    				}
    			}
    		}, {
    			text : '关 闭 ',
    			iconCls : 'stop',
    			handler : function() {
    				bannerWindow.hide();
    			}
    		} ]
    	});
    	
    	var picRenderer = function(value, metaData, record) {
    		if(value){
      			metaData.tdAttr = "data-qtip=\"<img src='/upload/banner/" + record.data.appid + '/' + value + "' style='width:320px; height:150px'/>\""; 
      			return '<img src="/upload/banner/' + record.data.appid + '/' + value + '" style="height:30px" onerror="this.src=\'/resources/img/noImage.png\'" />';
      		} else
      			return '<img src="/resources/img/noImage.png" style="height:30px" />';
        };
        
        var selModel = Ext.create('Ext.selection.CheckboxModel', {
    		injectCheckbox : 1,
    		mode : 'SINGLE'
    	});
    	
        if(!win){
            win = desktop.createWindow({
                id: 'banner-mgr',
                title:'Banner图片管理',
                width:850,
                height:540,
                iconCls: 'banner',
                animCollapse:false,
                constrainHeader:true,
                layout: 'fit',
                items: [{
                		border: false,
                        xtype: 'grid',
                        store: store,
                        selModel: selModel,
                        stripeRows : true,
                        frame : false,
                        viewConfig : {enableTextSelection : true},
                		loadMask : {msg : '正在加载表格数据,请稍等...'},
                        columns: [new Ext.grid.RowNumberer({
	                        	header : 'No',
	                    		width : '4%'
	                        }), {
	                    		dataIndex : 'id',
	                    		hidden: true
	                        },{
	                        	text : '图片',
	                        	dataIndex : 'bannerUrl',
	                    		width : '27%',
	                    		renderer: picRenderer
	                        },{
	                        	text : '链接地址',
	                    		dataIndex : 'linkUrl',
	                    		width : '55%'
	                        },{
	                        	text : '排序',
	                        	dataIndex : 'sortNo',
	                        	width : '10%'
	                        }]
	                    }, bannerWindow = Ext.create('Ext.Window', {
	                        title: '新增',
	                        width: 400,
	                        height: 220,
	                        constrain: true,
	                        layout: 'fit',
	                        items: [bannerForm]
	                    })
                ],
                tbar:[{
                    text:'新增',
                    iconCls:'add',
                    handler : function() {
                    	bannerWindow.setTitle('新建Banner');
                    	bannerForm.getForm().reset();
                    	bannerForm.getForm().url = '/biz/setting/insertBanner.atc';
                    	bannerWindow.show();
        			}
//                }, {
//                    text:'修改',
//                    iconCls:'pencil',
//                    handler : function() {
//                    	Ext.example.msg('asdf','asdffff');
//        			}
                }, {
                    text:'删除',
                    iconCls:'delete',
                    handler: function(){
                    	var record = selModel.getSelection()[0];
                		if (Ext.isEmpty(record)) {
                			Ext.MessageBox.show({
                				title : '提示',
                				msg : '你没有选中任何项目！',
                				buttons : Ext.MessageBox.OK,
                				icon : Ext.MessageBox.INFO
                			});
                			return;
                		}
                		Ext.Msg.confirm('请确认', '确定要删除这项吗?', function(btn, text) {
                			if (btn == 'yes') {
                				Ext.Ajax.request({
                					url : '/biz/setting/deleteBanner.atc',
                					params : {
                						'banner.id' : record.data.id
                					},
                					success : function(resp, opts) {
                						var result = Ext.decode(resp.responseText);
                						if (result.success) {
                							desktop.showMessage(result.msg);
                							selModel.deselectAll();
                							store.reload();
                						} else
                							Ext.MessageBox.show({
                								title : '提示',
                								msg : result.msg,
                								buttons : Ext.MessageBox.OK,
                								icon : Ext.MessageBox.ERROR
                							});
                					},
                					failure : function(resp, opts) {
                						var result = Ext.decode(resp.responseText);
                						Ext.MessageBox.show({
                							title : '提示',
                							msg : result.msg,
                							buttons : Ext.MessageBox.OK,
                							icon : Ext.MessageBox.ERROR
                						});
                					}
                				});
                			}
                		});
                    }
                }],
//                bbar:bbar,
                listeners: {
                    show: function() {
                    	store.load();
                    }
                }
            });
        }
        
        return win;
    }
    
    

});

