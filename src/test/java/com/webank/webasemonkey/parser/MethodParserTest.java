/**
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webank.webasemonkey.parser;

import java.util.List;
import java.util.Map;

import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.protocol.core.methods.response.AbiDefinition.NamedType;
import org.fisco.bcos.web3j.protocol.core.methods.response.AbiDefinition.NamedType.Type;
import org.fisco.bcos.web3j.tx.txdecode.BaseException;
import org.fisco.bcos.web3j.tx.txdecode.ContractAbiUtil;
import org.fisco.bcos.web3j.tx.txdecode.DynamicArrayReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.webank.webasemonkey.WebasemonkeyApplicationTests;
import com.webank.webasemonkey.tools.JacksonUtils;
import com.webank.webasemonkey.vo.FieldVO;
import com.webank.webasemonkey.vo.MethodMetaInfo;


/**
 * MethodParserTest
 *
 * @Description: MethodParserTest
 * @author maojiayu
 * @data Apr 17, 2020 3:27:00 PM
 *
 */
public class MethodParserTest extends WebasemonkeyApplicationTests {
    @Autowired
    private MethodParser methodParser;

    @SuppressWarnings("unchecked")
    @Test
    public void testGetField() throws BaseException, ClassNotFoundException {
        String methodMetaInfoStr =
                "{\"contractName\":\"AccessRestriction\",\"name\":\"revokeUser\",\"shardingNO\":1,\"list\":null}";
        String inputsAddress = "[{\"name\":\"_user\",\"type\":\"address\",\"type0\":null,\"indexed\":false}]";
        String fieldList =
                "[{\"sqlName\":\"_user_\",\"solidityName\":\"_user\",\"javaName\":\"_user\",\"sqlType\":\"varchar(255)\",\"solidityType\":\"Address\",\"javaType\":\"String\",\"entityType\":null,\"typeMethod\":\"AddressUtils.bigIntegerToString\",\"javaCapName\":\"_user\",\"length\":0}]";
        MethodMetaInfo mmi = JacksonUtils.fromJson(methodMetaInfoStr, MethodMetaInfo.class);
        List<NamedType> nt = JacksonUtils.fromJson(inputsAddress, List.class, NamedType.class);
        List<FieldVO> list = methodParser.getFieldList(mmi, nt);
        
        String methodMetaInfoStaticArrayStr =
                "{\"contractName\":\"RecordData\",\"name\":\"insertRecord\",\"shardingNO\":1,\"list\":null}";
        String inputsStaticArray = "[{\"name\":\"record\",\"type\":\"bytes[]\",\"type0\":null,\"indexed\":false}]";
        String fieldList2 =
                "[{\"sqlName\":\"_record_\",\"solidityName\":\"record\",\"javaName\":\"record\",\"sqlType\":\"varchar(10240)\",\"solidityType\":\"DynamicArray<bytes>\",\"javaType\":\"String\",\"entityType\":null,\"typeMethod\":\"BytesUtils.dynamicBytesListObjectToString\",\"javaCapName\":\"Record\",\"length\":0}]";
        MethodMetaInfo mmi2 = JacksonUtils.fromJson(methodMetaInfoStaticArrayStr, MethodMetaInfo.class);
        List<NamedType> nt2 = JacksonUtils.fromJson(inputsStaticArray, List.class, NamedType.class);
        List<FieldVO> list2 = methodParser.getFieldList(mmi2, nt2);
        
        List<TypeReference<?>> listOfTypeReference = ContractAbiUtil.paramFormat(nt);
        System.out.println(JacksonUtils.toJson(listOfTypeReference));
        System.out.println(JacksonUtils.toJson(ContractAbiUtil.paramFormat(nt2)));
        
        for(NamedType n : nt2) {
           Type type = new Type(n.getType());
           System.out.println(JacksonUtils.toJson(type));
           TypeReference<?> tr = DynamicArrayReference.create(type.getBaseName(), n.isIndexed());
           System.out.println(tr.getClass().getSimpleName());
        }     
    }
    
    
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        String s = "[{\"value\":\"aw==\",\"typeAsString\":\"bytes1\"},{\"value\":\"dg==\",\"typeAsString\":\"bytes1\"}]";
        List<Map<String, byte[]>> list = JacksonUtils.fromJson(s, List.class, Map.class);
        System.out.println(JacksonUtils.toJson(list.get(0).get("value")));
    }

}
