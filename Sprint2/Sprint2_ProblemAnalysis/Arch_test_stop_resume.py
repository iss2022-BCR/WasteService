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
with Diagram('test_stop_resumeArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctx_stopresume_test', graph_attr=nodeattr):
          transporttrolley_sr_test=Custom('transporttrolley_sr_test','./qakicons/symActorSmall.png')
          pathexecutorbcr_sr_test=Custom('pathexecutorbcr_sr_test','./qakicons/symActorSmall.png')
          alarmcontroller_sr_test=Custom('alarmcontroller_sr_test','./qakicons/symActorSmall.png')
     transporttrolley_sr_test >> Edge(color='magenta', style='solid', xlabel='dopath', fontcolor='magenta') >> pathexecutorbcr_sr_test
     sys >> Edge(color='red', style='dashed', xlabel='stop', fontcolor='red') >> pathexecutorbcr_sr_test
     pathexecutorbcr_sr_test >> Edge(color='darkgreen', style='dashed', xlabel='dopathdone', fontcolor='darkgreen') >> transporttrolley_sr_test
     pathexecutorbcr_sr_test >> Edge(color='darkgreen', style='dashed', xlabel='dopathfail', fontcolor='darkgreen') >> transporttrolley_sr_test
     sys >> Edge(color='red', style='dashed', xlabel='resume', fontcolor='red') >> pathexecutorbcr_sr_test
     alarmcontroller_sr_test >> Edge( xlabel='stop', **eventedgeattr, fontcolor='red') >> sys
     alarmcontroller_sr_test >> Edge( xlabel='resume', **eventedgeattr, fontcolor='red') >> sys
diag
