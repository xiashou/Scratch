
Ext.define('SmartPlatform.ux.tree.TreeGrid', {
    extend: 'Ext.tree.Panel',
    
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.tree.*',
        'Ext.ux.CheckColumn',
        'KitchenSink.model.tree.Task'
    ],    
    xtype: 'tree-grid',
    
    
    title: 'Core Team Projects',
    height: 300,
    useArrows: true,
    rootVisible: false,
    multiSelect: true,
    singleExpand: true,
    
    initComponent: function() {
        this.width = 600;
        
        
        this.callParent();
    }
});