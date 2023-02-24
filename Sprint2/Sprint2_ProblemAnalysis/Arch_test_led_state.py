from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted'
}
with Diagram('test_led_stateArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctx_ledstate_test', graph_attr=nodeattr):
          transporttrolley_ls_test=Custom('transporttrolley_ls_test','./qakicons/symActorSmall.png')
          pathexecutorbcr_ls_test=Custom('pathexecutorbcr_ls_test','./qakicons/symActorSmall.png')
          trolleystateprovider_ls_test=Custom('trolleystateprovider_ls_test','./qakicons/symActorSmall.png')
          alarmcontroller_ls_test=Custom('alarmcontroller_ls_test','./qakicons/symActorSmall.png')
          ledcontroller_ls_test=Custom('ledcontroller_ls_test','./qakicons/symActorSmall.png')
     transporttrolley_ls_test >> Edge(color='magenta', style='solid', xlabel='dopath', fontcolor='magenta') >> pathexecutorbcr_ls_test
     sys >> Edge(color='red', style='dashed', xlabel='stop', fontcolor='red') >> pathexecutorbcr_ls_test
     pathexecutorbcr_ls_test >> Edge(color='darkgreen', style='dashed', xlabel='dopathdone', fontcolor='darkgreen') >> transporttrolley_ls_test
     pathexecutorbcr_ls_test >> Edge(color='darkgreen', style='dashed', xlabel='dopathfail', fontcolor='darkgreen') >> transporttrolley_ls_test
     sys >> Edge(color='red', style='dashed', xlabel='resume', fontcolor='red') >> pathexecutorbcr_ls_test
     transporttrolley_ls_test >> Edge(color='blue', style='solid', xlabel='coapUpdate', fontcolor='blue') >> trolleystateprovider_ls_test
     pathexecutorbcr_ls_test >> Edge(color='blue', style='solid', xlabel='coapUpdate', fontcolor='blue') >> trolleystateprovider_ls_test
     trolleystateprovider_ls_test >> Edge( xlabel='trolley_state_changed', **eventedgeattr, fontcolor='red') >> sys
     alarmcontroller_ls_test >> Edge( xlabel='stop', **eventedgeattr, fontcolor='red') >> sys
     alarmcontroller_ls_test >> Edge( xlabel='resume', **eventedgeattr, fontcolor='red') >> sys
     sys >> Edge(color='red', style='dashed', xlabel='trolley_state_changed', fontcolor='red') >> ledcontroller_ls_test
diag
