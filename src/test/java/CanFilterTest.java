import org.graylog.plugins.pipelineprocessor.EvaluationContext;
import org.graylog.plugins.pipelineprocessor.ast.expressions.Expression;
import org.graylog.plugins.pipelineprocessor.ast.functions.Function;
import org.graylog.plugins.pipelineprocessor.ast.functions.FunctionArgs;
import org.graylog.plugins.pipelineprocessor.ast.functions.FunctionDescriptor;
import org.graylog2.plugin.Message;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rule.plugin.CanFilterFunction;

@RunWith(MockitoJUnitRunner.class)
public class CanFilterTest {
    FunctionArgs functionArgs;
    CanFilterFunction function;
    Message message;
    EvaluationContext evaluationContext;

    @Before
    public void setUp() {
        function = new CanFilterFunction();
        message = new Message("__dummy", "__dummy", DateTime.parse("2010-07-30T16:03:25Z"));
        evaluationContext = new EvaluationContext(message);
        functionArgs = new FunctionArgs(new Function<String>() {

            @Override
            public Object preComputeConstantArgument(FunctionArgs args, String name, Expression arg) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String evaluate(FunctionArgs args, EvaluationContext context) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public FunctionDescriptor<String> descriptor() {
                // TODO Auto-generated method stub
                return null;
            }
        }, null);
    }

    @Test
    public void testWithFieldsAndCsvValuesSameLengthAndDefaultSeparator(){
        String message = "{\"component_port\":\"262148\",\"type\":\"VSOC\",\"timestamp\":\"2025-12-09T14:46:18.669340+0000\",\"endpoint_id\":\"LL3BKCJ75SA080045\",\"timestamp_be\":\"2025-12-09T06:46:19.769349+0000\",\"alert\":[{\"can_id\":\"0x675\",\"category\":\"incorrect id detected\",\"can_data\":\"\",\"err_code\":\"0x1\",\"signature\":\"can id error\",\"can_rtr\":\"data\",\"can_ide\":\"ext\",\"severity\":1,\"can_ch\":4,\"bus_load\":2,\"can_dlc\":8}],\"@version\":\"1\",\"proto\":\"can\",\"endpoint_alias\":\"jinlv_bus\",\"ids_type\":\"endpoint_can_ids\",\"project_alias\":\"XiamenJinlv\",\"component_ecu\":\"TBOX\",\"endpoint_model\":\"busD01\",\"component_type\":\"CAN_IDS\",\"@timestamp\":\"2025-12-09T06:46:19.929Z\",\"prober_type\":\"can\"}";
        setFields(message, "0x1");

        @SuppressWarnings("unchecked")
        final Boolean result1 = function.evaluate(functionArgs, evaluationContext);
        System.out.println(result1);

        @SuppressWarnings("unchecked")
        final Boolean result2 = function.evaluate(functionArgs, evaluationContext);
        System.out.println(result2);
    }

    public void setFields(String message, String err_code) {
        functionArgs.setPreComputedValue("message", message);
        functionArgs.setPreComputedValue("err_code", err_code);
    }
}
