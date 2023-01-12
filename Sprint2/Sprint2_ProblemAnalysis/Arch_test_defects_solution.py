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
with Diagram('test_defects_solutionArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctx_defects_solution_test', graph_attr=nodeattr):
          smartdevice_ds_test=Custom('smartdevice_ds_test','./qakicons/symActorSmall.png')
          wasteservice_ds_test=Custom('wasteservice_ds_test','./qakicons/symActorSmall.png')
          transporttrolley_ds_test=Custom('transporttrolley_ds_test','./qakicons/symActorSmall.png')
diag
