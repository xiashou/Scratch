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
            text: 'Banner图片设置',
            iconCls:'banner'
        };
    },

    createWindow : function(){
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('banner-mgr');
        var store = new Ext.data.Store({
    		pageSize : 20,
    		fields: ['id', 'bannerUrl', 'linkUrl'],
    		proxy : {
    			type : 'ajax',
    			url : '/biz/setting/queryBannerList.atc',
    			reader : {
    				root : 'bannerList'
    			}
    		}
    	});
        var pagesizeCombo = desktop.getPagesizeCombo();
    	var number = parseInt(pagesizeCombo.getValue());
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
    	
    	var bbar = new Ext.PagingToolbar({
    		pageSize : number,
    		store : store,
    		displayInfo : true,
    		displayMsg : '显示{0}条到{1}条,共{2}条',
    		emptyMsg : "没有符合条件的记录",
    		items : [ '-', '&nbsp;&nbsp;', pagesizeCombo ]
    	});
    	
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
//    				id : 'sort',
//    				xtype : 'numberfield',
//    				name : 'banner.sort',
//    				fieldLabel : '排序',
//    			},{
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
    				xtype : 'displayfield',
    				labelWidth : 70
    			},
    			items: [ {
    				fieldLabel : '&nbsp;',
    				labelSeparator: '',
    				value:'图片最佳尺寸640*300'
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
    							Ext.example.msg('提示', action.result.msg);
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
    	
    	
        if(!win){
            win = desktop.createWindow({
                id: 'banner-mgr',
                title:'Banner图片管理',
                width:800,
                height:520,
                iconCls: 'banner',
                animCollapse:false,
                constrainHeader:true,
                layout: 'fit',
                items: [{
                		border: false,
                        xtype: 'grid',
                        store: store,
                        columns: [new Ext.grid.RowNumberer(), {
	                        	text : '编码',
	                    		dataIndex : 'id',
	                    		width : 80,
	                    		hidden: true
                            },{
                            	text : '图片',
                        		width : 280,
                        		dataIndex : 'bannerUrl'
                            },{
                            	text : '链接地址',
                        		dataIndex : 'linkUrl',
                        		width : 100
                            }]
                    }, bannerWindow = Ext.create('Ext.Window', {
                        title: '新增',
                        width: 400,
                        height: 200,
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
                }, {
                    text:'修改',
                    iconCls:'pencil'
                }, {
                    text:'删除',
                    iconCls:'delete'
                }],
                bbar:bbar,
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

