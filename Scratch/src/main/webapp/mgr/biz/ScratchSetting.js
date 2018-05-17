/*
 * 刮奖设置
 * xs
 * 2018/04/24
 */
Ext.define('Business.ScratchSetting', {
	extend: 'Ext.ux.desktop.Module',

    requires: [
        'Ext.util.Format',
        'Ext.grid.Panel',
        'Ext.grid.RowNumberer'
    ],

    id:'scratch-set',

    init : function(){
        this.launcher = {
            text: '刮奖设置',
            iconCls:'setting'
        };
    },
	
    store: new Ext.data.Store({
		pageSize : 20,
		fields: ['id', 'actId', 'name', 'price', 'probability', 'createdTime'],
		proxy : {
			type : 'ajax',
			url : '/biz/scratch/queryScratchList.atc',
			reader : {
				root : 'scratchList'
			}
		}
	}),
	
	f: new Ext.form.FormPanel({
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
				name : 'scratch.name',
				fieldLabel : '名称',
			},{
				name : 'scratch.actId',
				xtype: 'hiddenfield'
			},{
				name : 'scratch.id',
				xtype: 'hiddenfield'
			}]
		},{
			defaults : {flex : 1,xtype : 'numberfield',labelWidth : 70,labelAlign : 'right'},
			items : [ {
				name : 'scratch.price',
				fieldLabel : '金额（元）'
			},{
				name: 'scratch.probability',
				fieldLabel : '概率（%）'
			} ]
		} ]
	}),
	
	buttons: function(me){
		var desktop = me.app.getDesktop();
		return [{
			text : '保 存',
			iconCls : 'accept',
			handler : function() {
				if (me.f.form.isValid()) {
					me.f.form.submit({
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
							subwin_scrsetting.hide();
							desktop.showMessage(action.result.msg);
							me.store.reload();
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
				subwin_scrsetting.hide();
			}
		}];
	},
	
	f1: new Ext.form.FormPanel({
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
				name : 'scratchText.name',
				fieldLabel : '未中奖字样',
			},{
				name : 'scratchText.actId',
				xtype: 'hiddenfield'
			},{
				name : 'scratchText.id',
				xtype: 'hiddenfield'
			}]
		},{
			defaults : {flex : 1,xtype : 'textarea',labelWidth : 70,labelAlign : 'right'},
			items : [ {
				name : 'scratchText.describ',
				fieldLabel : '刮奖说明',
				rows:7
			} ]
		} ]
	}),
	
	buttons1: function(me){
		var desktop = me.app.getDesktop();
		return [{
			text : '保 存',
			iconCls : 'accept',
			handler : function() {
				if (me.f1.form.isValid()) {
					me.f1.form.submit({
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
							subwin_scrother.hide();
							desktop.showMessage(action.result.msg);
							me.store.reload();
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
				subwin_scrother.hide();
			}
		}];
	},
	
	selModel: Ext.create('Ext.selection.CheckboxModel', {
		injectCheckbox : 1,
		mode : 'SINGLE'
	}),
	
	tbar: function(me) {
		var desktop = me.app.getDesktop();
		return [{
            text:'新增',
            iconCls:'add',
            handler : function() {
            	subwin_scrsetting.setTitle('新建');
            	me.f.getForm().reset();
            	me.f.getForm().url = '/biz/scratch/insertScratch.atc';
            	subwin_scrsetting.show();
			}
        }, {
            text:'修改',
            iconCls:'pencil',
            handler : function() {
            	var record = me.selModel.getSelection()[0];
        		if (Ext.isEmpty(record)) {
        			Ext.MessageBox.show({
        				title : '提示',
        				msg : '你没有选中任何项目！',
        				buttons : Ext.MessageBox.OK,
        				icon : Ext.MessageBox.INFO
        			});
        			return;
        		}
        		subwin_scrsetting.setTitle('修改优惠券');
        		me.f.getForm().reset();
            	me.f.getForm().url = '/biz/scratch/updateScratch.atc';
            	me.selModel.deselectAll();
            	me.f.getForm().setValues([
            		{id:'scratch.id', value:record.data.id},
					{id:'scratch.actId', value:record.data.actId},
					{id:'scratch.name', value:record.data.name},
					{id:'scratch.price', value:record.data.price},
					{id:'scratch.probability', value:record.data.probability}
				]);
            	subwin_scrsetting.show();
			}
        }, {
            text:'删除',
            iconCls:'delete',
            handler: function(){
            	var record = me.selModel.getSelection()[0];
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
        					url : '/biz/scratch/deleteScratch.atc',
        					params : {
        						'scratch.id' : record.data.id
        					},
        					success : function(resp, opts) {
        						var result = Ext.decode(resp.responseText);
        						if (result.success) {
        							desktop.showMessage(result.msg);
        							me.selModel.deselectAll();
        							me.store.reload();
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
        },{
        	text:'其他设置',
            iconCls:'table_edit',
            handler : function() {
            	me.completeText(me);
            	subwin_scrother.setTitle('修改');
            	me.f1.getForm().reset();
            	me.f1.getForm().url = '/biz/scratch/updateTextScratch.atc';
            	subwin_scrother.show();
			}
        }]
	},
	
	completeText : function(me){
		Ext.Ajax.request({
			url : '/biz/scratch/queryScratchText.atc',
			success : function(resp, opts) {
				var result = Ext.decode(resp.responseText);
				if(result && !Ext.isEmpty(result)){
					me.f1.getForm().setValues([
	            		{id:'scratchText.id', value:result.id},
						{id:'scratchText.actId', value:result.actId},
						{id:'scratchText.name', value:result.name},
						{id:'scratchText.describ', value:result.describ}
					]);
				}
			}
		});
	},

    createWindow : function(){
    	var me = this;
        var desktop = me.app.getDesktop();
        var win = desktop.getWindow('scratch-set');
    	
        if(!win){
            win = desktop.createWindow({
                id: 'scratch-set',
                title:'刮奖设置',
                width:800,
                height:480,
                iconCls: 'setting',
                animCollapse:false,
                constrainHeader:true,
                layout: 'fit',
                items: [{
                		border: false,
                        xtype: 'grid',
                        store: me.store,
                        selModel: me.selModel,
                        stripeRows : true,
                        frame : false,
                        forceFit: true,
                        viewConfig : {enableTextSelection : true},
                		loadMask : {msg : '正在加载表格数据,请稍等...'},
                        columns: [new Ext.grid.RowNumberer({
	                        	header : 'No',
	                    		width : '4%'
	                        }), {
	                    		dataIndex : 'id',
	                    		hidden: true
	                        },{
	                        	text : '名称',
	                        	dataIndex : 'name',
	                    		width : '30%'
	                        },{
	                        	text : '金额（元）',
	                    		dataIndex : 'price',
	                    		width : '20%'
	                        },{
	                        	text : '概率（%）',
	                        	dataIndex : 'probability',
	                        	width : '20%'
	                        },{
	                        	text : '创建时间',
	                        	dataIndex : 'createdTime',
	                        	width : '30%'
	                        }]
	                    }, subwin_scrsetting = Ext.create('Ext.Window', {
	                        width: 360,
	                        height: 180,
	                        constrain: true,
	                        layout: 'fit',
	                        closeAction: 'hide',
	                        items: [me.f],
	                        buttons: me.buttons(me)
	                    }), subwin_scrother = Ext.create('Ext.Window', {
	                        width: 360,
	                        height: 250,
	                        constrain: true,
	                        layout: 'fit',
	                        closeAction: 'hide',
	                        items: [me.f1],
	                        buttons: me.buttons1(me)
	                    })
                ],
                tbar: me.tbar(me),
                listeners: {
                    show: function() {
                    	me.store.load();
                    }
                }
            });
        }
        
        return win;
    }
    
    

});

