package org.rule.plugin;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.graylog.plugins.pipelineprocessor.EvaluationContext;
import org.graylog.plugins.pipelineprocessor.ast.expressions.Expression;
import org.graylog.plugins.pipelineprocessor.ast.functions.*;
import org.rule.plugin.util.CacheUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
public class CanFilterFunction implements Function<Boolean> {
    public static final String NAME = "can_filter";
    private static final String PARAM1 = "message";
    private static final String PARAM2 = "err_code";


    private final ParameterDescriptor<String, String> valueParam1 = ParameterDescriptor
            .string(PARAM1)
            .description("message filter by can err_code.")
            .build();

    private final ParameterDescriptor<String, String> valueParam2 = ParameterDescriptor
            .string(PARAM2)
            .description("message filter by can err_code.")
            .build();

    @Override
    public Object preComputeConstantArgument(FunctionArgs functionArgs, String s, Expression expression) {
        return expression.evaluateUnsafe(EvaluationContext.emptyContext());
    }

    @Override
    public Boolean evaluate(FunctionArgs functionArgs, EvaluationContext evaluationContext) {
        String target = valueParam1.required(functionArgs, evaluationContext);
        String target2 = valueParam2.required(functionArgs, evaluationContext);

        if (target == null || target.isBlank() || target2 == null || target2.isBlank()) {
            return false;
        }

        Set<String> errCodeSet = Arrays.stream(target2.split(",")).collect(Collectors.toSet());
        try {
            JSONObject message = JSON.parseObject(target);
            if (message.containsKey("alert") && message.get("alert") instanceof JSONArray) {
                JSONObject alert = message.getJSONArray("alert").getJSONObject(0);
                if (!alert.containsKey("err_code") || !message.containsKey("endpoint_id")) {
                    return false;
                }
                String endpointId = message.getString("endpoint_id");
                String canId = alert.getString("can_id");
                String errCode = alert.get("err_code").toString();
                if (errCode == null || errCode.isBlank()) {
                    return false;
                }
                if (errCodeSet.contains(errCode.trim())) {
                    return false;
                }

                String key = endpointId + "_" + canId + "_" + errCode;
                if (CacheUtils.get(key) == null) {
                    CacheUtils.set(key, 1, 86400L);
                    return false;
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public FunctionDescriptor<Boolean> descriptor() {
        return FunctionDescriptor.<Boolean>builder()
                .name(NAME)
                .description("return result of filtered 0x1 or 0x4 err.")
                .params(valueParam1, valueParam2)
                .returnType(Boolean.class)
                .build();
    }
}
