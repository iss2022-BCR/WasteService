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
with Diagram('test_waste_serviceArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctx_wasteservice_test', graph_attr=nodeattr):
          wasteservice_ws_test=Custom('wasteservice_ws_test','./qakicons/symActorSmall.png')
          transporttrolley_ws_test=Custom('transporttrolley_ws_test','./qakicons/symActorSmall.png')
     wasteservice_ws_test >> Edge(color='magenta', style='solid', xlabel='deposit', fontcolor='magenta') >> transporttrolley_ws_test
     transporttrolley_ws_test >> Edge(color='blue', style='solid', xlabel='depositcompleted', fontcolor='blue') >> wasteservice_ws_test
diag
