package com.amereco.utils;

import com.amereco.json.EdgeFormData;

import java.util.List;

public class Utils {
    public static String getValue(List<EdgeFormData> edgeFormDatas,String label){
        for(EdgeFormData edgeFormData : edgeFormDatas){
            if(edgeFormData.getLabel().equals(label)){
                return edgeFormData.getValue();
            }
        }
        return "";
    }

    public static void updateValue(List<EdgeFormData> edgeFormDatas,String label,String value){
        for(EdgeFormData edgeFormData : edgeFormDatas){
            if(edgeFormData.getLabel().equals(label)){
                edgeFormData.setValue(value);
                return;
            }
        }
    }
}
