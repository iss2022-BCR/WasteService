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
with Diagram('test_transport_trolleyArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctx_transporttrolley_test', graph_attr=nodeattr):
          wasteservice_tt_test=Custom('wasteservice_tt_test','./qakicons/symActorSmall.png')
          transporttrolley_tt_test=Custom('transporttrolley_tt_test','./qakicons/symActorSmall.png')
     wasteservice_tt_test >> Edge(color='magenta', style='solid', xlabel='deposit', fontcolor='magenta') >> transporttrolley_tt_test
     transporttrolley_tt_test >> Edge(color='blue', style='solid', xlabel='depositcompleted', fontcolor='blue') >> wasteservice_tt_test
diag
