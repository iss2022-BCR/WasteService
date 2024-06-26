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
with Diagram('interaction_sd_wsArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctx_test_storerequest', graph_attr=nodeattr):
          test_wasteservice=Custom('test_wasteservice','./qakicons/symActorSmall.png')
     test_wasteservice >> Edge(color='green', style='dashed', xlabel='loadaccepted') >> sys 
     test_wasteservice >> Edge(color='green', style='dashed', xlabel='loadrejected') >> sys 
diag
