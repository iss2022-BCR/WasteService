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
with Diagram('test_messages2Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctx_pc', graph_attr=nodeattr):
          pc=Custom('pc','./qakicons/symActorSmall.png')
     with Cluster('ctx_rpi', graph_attr=nodeattr):
          rpi=Custom('rpi(ext)','./qakicons/externalQActor.png')
     sys >> Edge(color='red', style='dashed', xlabel='eventMessage', fontcolor='red') >> pc
     pc >> Edge(color='blue', style='solid', xlabel='ack', fontcolor='blue') >> rpi
diag
