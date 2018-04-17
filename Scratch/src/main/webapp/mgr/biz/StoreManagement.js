/*
 * 商户管理
 * xs
 * 2018/04/03
 */
Ext.define('Business.StoreManagement', {
	extend: 'Ext.ux.desktop.Module',

    requires: [
        'Ext.util.Format',
        'Ext.grid.Panel',
        'Ext.grid.RowNumberer'
    ],

    id:'store-mgr',

    init : function(){
        this.launcher = {
            text: '商户管理',
            iconCls:'store'
        };
    },

    createWindow : function(){
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('store-mgr');
        var store = new Ext.data.Store({
    		pageSize : 500,
    		fields: ['id', 'appid', 'name', 'address', 'phone', 'introduction', 'headUrl', 'imageUrl', 'locationx', 'locationy', 'enable', 'createdTime'],
    		proxy : {
    			type : 'ajax',
    			url : '/biz/store/queryListByAppid.atc',
    			reader : {
    				root : 'storeList'
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
	  	
	  	var f = new Ext.form.FormPanel({
    		id : 'f',
    		layout : 'anchor',
    		defaults : {
    			anchor : '100%',
    			layout : 'hbox',
    			xtype : 'fieldcontainer'
    		},
    		bodyPadding : '5 10 0 0',
    		border : false,
    		items : [ {
    			defaults : {flex : 1,xtype : 'textfield',labelWidth : 70,labelAlign : 'right'},
    			items : [ {
    				id : 'name',
    				name : 'store.name',
    				fieldLabel : '商户名称',
    			},{
    				id : 'phone',
    				name : 'store.phone',
    				fieldLabel : '联系电话',
    			},{
    				id : 'id',
    				name : 'store.id',
    				xtype: 'hiddenfield'
    			}]
    		},{
    			defaults : {flex : 1,xtype : 'textfield',labelWidth : 70,labelAlign : 'right'},
    			items : [{
    				id : 'address',
    				name: 'store.address',
    				fieldLabel : '地址'
    			}]
    		},{
    			defaults : {flex : 8,xtype : 'textfield',labelWidth : 70,labelAlign : 'right'},
    			items: [ {
    				id : 'locationx',
    				name: 'store.locationx',
    				fieldLabel : '地址x坐标',
    				readOnly:true, 
    				fieldStyle:'background-color: #F0F0F0;'
    			},{
    				id : 'locationy',
    				name: 'store.locationy',
    				fieldLabel : '地址y坐标',
    				readOnly:true, 
    				fieldStyle:'background-color: #F0F0F0;'
    			},{
    				xtype: 'button',
    				flex : 3,
    				margin: '0 0 0 5',
    				text: '选取地址',
    				handler: function() {
    					mw.update('<iframe src="/mgr/ux/map/map.jsp" width="100%" height="100%" frameborder="0"></iframe>');
                    	mw.show();
    			    }
    			}]
    		},{
    			defaults : {flex : 1,xtype : 'textarea',labelWidth : 70,labelAlign : 'right'},
    			items : [ {
    				fieldLabel : '商户简介',
    				id : 'introduction',
    				name: 'store.introduction',
    				rows: 3
    			} ]
    		},{
    			defaults : {flex : 3,xtype : 'filefield',labelWidth : 70,labelAlign : 'right'},
    			items : [ {
    				fieldLabel : '小图',
    				id : 'head',
    				name: 'head',
					buttonText: '浏览...',
    				anchor : '99%'
    			},{
    				xtype : 'displayfield',
    				flex : 1,
					labelWidth : 5,
    				fieldLabel : '&nbsp;',
    				labelSeparator: '',
    				value:'最佳尺寸180*180'
    			} ]
    		},{
    			defaults : {flex : 3,xtype : 'filefield',labelWidth : 70,labelAlign : 'right'},
    			items : [ {
    				fieldLabel : '大图',
    				id : 'image',
    				name: 'image',
    				buttonText: '浏览...',
    				anchor : '99%'
    			},{
    				xtype : 'displayfield',
    				flex : 1,
    				labelWidth : 5,
    				fieldLabel : '&nbsp;',
    				labelSeparator: '',
    				value:'最佳尺寸750*408'
    			} ]
    		},{
    			defaults : {flex : 1,xtype : 'radiogroup',labelWidth : 70,labelAlign : 'right'},
    			items: [{
    				id:'enable', 
    				fieldLabel: '是否可用', 
    				defaults: {name: 'store.enable'},
		        	items: [{inputValue: true, boxLabel: '正常', checked: true}, {inputValue: false,boxLabel: '锁定'}]
    			},{
    				xtype : 'displayfield',
    				flex : 1,
    				fieldLabel : '&nbsp;',
    				labelSeparator: '',
    				value:''
    			}]
    		} ],
    		buttons : [ {
    			text : '保 存',
    			iconCls : 'accept',
    			handler : function() {
    				if (f.form.isValid()) {
    					f.form.submit({
    						waitTitle : '提示',
    						method : 'POST',
    						waitMsg : '正在处理数据,请稍候...',
    						success : function(form, action) {
    							w.hide();
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
    				mw.hide();
    				w.hide();
    			}
    		} ]
    	});
	  	
	  	var smallRenderer = function(value, metaData, record) {
    		if(value){
      			metaData.tdAttr = "data-qtip=\"<img src='/upload/store/" + record.data.appid + '/' + value + "' style='width:90px; height:90px'/>\""; 
      			return '<img src="/upload/store/' + record.data.appid + '/' + value + '" style="height:20px" onerror="this.src=\'/resources/img/noImage.png\'" />';
      		} else
      			return '<img src="/resources/img/noImage.png" style="height:20px" />';
        };
        
        var largeRenderer = function(value, metaData, record) {
    		if(value){
      			metaData.tdAttr = "data-qtip=\"<img src='/upload/store/" + record.data.appid + '/' + value + "' style='width:375px; height:204px'/>\""; 
      			return '<img src="/upload/store/' + record.data.appid + '/' + value + '" style="height:20px" onerror="this.src=\'/resources/img/noImage.png\'" />';
      		} else
      			return '<img src="/resources/img/noImage.png" style="height:20px" />';
        };
	  	
	  	var selModel = Ext.create('Ext.selection.CheckboxModel', {
    		injectCheckbox : 1,
    		mode : 'SINGLE'
    	});
	  	
        if(!win){
            win = desktop.createWindow({
                id: 'store-mgr',
                title:'商户管理',
                width:900,
                height:580,
                iconCls: 'store',
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
	                    	text : '店名',
	                    	dataIndex : 'name',
	                		width : '15%'
	                    },{
	                    	text : '联系电话',
	                    	dataIndex : 'phone',
	                    	width : '12%'
	                    },{
	                    	text : '地址',
	                		dataIndex : 'address',
	                		width : '25%'
	                    },{
	                    	text : '小图',
	                    	dataIndex : 'headUrl',
	                    	width : '8%',
	                    	renderer: smallRenderer
	                    },{
	                    	text : '大图',
	                    	dataIndex : 'imageUrl',
	                    	width : '8%',
	                    	renderer: largeRenderer
	                    },{
	                    	text : '是否可用',
	                    	dataIndex : 'enable',
	                    	width : '8%',
	                    	renderer: function(value){
	            				return value ? '<font color=green>正常</font>' : '<font color=red>锁定</font>'
	            			}
	                    },{
	                    	text : '创建时间',
	                    	dataIndex : 'createdTime',
	                    	width : '16%'
	                    }]
                    }, w = Ext.create('Ext.Window', {
                        width: 520,
                        height: 350,
                        constrain: true,
                        layout: 'fit',
                        items: [f]
                    }), mw = Ext.create('Ext.Window', {
                    	width: 635,
                        height: 610,
                        title:'选取地址',
                        constrain: false,
						closeAction:'hide',
                        layout: 'fit'
                    })
                ],
                tbar:[{
                    text:'新增',
                    iconCls:'add',
                    handler : function() {
                    	w.setTitle('新建商户');
                    	f.getForm().reset();
                    	f.getForm().url = '/biz/store/insertStore.atc';
                    	w.show();
        			}
                }, {
                    text:'修改',
                    iconCls:'pencil',
                    handler : function() {
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
                    	w.setTitle('修改商户');
                    	f.getForm().reset();
                    	f.getForm().url = '/biz/store/updateStore.atc';
                    	selModel.deselectAll();
                		f.loadRecord(record);
                		Ext.getCmp('enable').setValue({'store.enable' : record.data.enable});
                    	w.show();
        			}
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
                					url : '/biz/store/deleteStore.atc',
                					params : {
                						'store.id' : record.data.id
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

