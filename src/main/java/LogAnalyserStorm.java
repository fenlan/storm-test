import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

public class LogAnalyserStorm {
    public static void main(String[] args) throws Exception{
        //Create Config instance for cluster configuration
        Config config = new Config();
        config.setDebug(true);

        //
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("call-log-reader-spout", new FakeCallLogReaderSpout(), 2);

        builder.setBolt("call-log-creator-bolt", new CallLogCreatorBolt(), 4)
                .shuffleGrouping("call-log-reader-spout");

        builder.setBolt("call-log-counter-bolt", new CallLogCounterBolt(), 4)
                .fieldsGrouping("call-log-creator-bolt", new Fields("call"));

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("LogAnalyserStorm", config, builder.createTopology());
        Thread.sleep(10000);

        //Stop the topology
        cluster.killTopology("LogAnalyserStorm");
        cluster.shutdown();
    }
}