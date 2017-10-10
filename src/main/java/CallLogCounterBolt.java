import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class CallLogCounterBolt extends BaseRichBolt {

    Map<String, Integer> counterMap;
    private OutputCollector collector;
    private static Logger logger = Logger.getLogger("CallLogCounterBolt");

    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        this.counterMap = new HashMap<String, Integer>();
        this.collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
        String call = tuple.getString(0);
        Integer duration = tuple.getInteger(1);

        if(!counterMap.containsKey(call)){
            counterMap.put(call, 1);
        }else{
            Integer c = counterMap.get(call) + 1;
            counterMap.put(call, c);
        }

        logger.info(call + " : " + counterMap.get(call));
        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("call"));
    }
}