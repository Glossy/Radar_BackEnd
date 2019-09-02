import org.junit.Test;
import spring_with_netty.netty.util.DataProcessor;

import java.io.IOException;
import java.util.List;

/**
 * @Author: Wu
 * @Date: 2019/8/5 6:14 AM
 */
public class DataProcessorTest {

    @Test
    public void SerialDataOn() throws Exception{
        DataProcessor processor = new DataProcessor();
//        String hexString  = "020104030605080704000002a002000042160a006706000043d132e9040000000300000000000000010000003400000004000900030000008e06ceff2c000000fd0000006d0022f0590f000003000000da04bdff00000000fd0000004e00cc0d3c11000002000000000200007d7baa86218b178ddf895986e77c0b77da70f768c75d825a9d5464518042f7451b465e54295fb566aa65cb5c81594255055353537a4d324cfc4cbd4b9d48f24247442647254a9f50df507e4fe449be45e7457c40073ed13e854386443b42c5452843fb45df4483439143db447042a03fcd432d47b6469c422d48d94956442144ee47bf44e44444444b42a541af3c363da0427e4424437c3e0b42c1414c416240f33d8a41c0442c43c244ce42c0406642a4441441043f11440142f644aa450444d6412444014393438a463d4568452343ce4152404d41ca3fa539ba3d4d3ef73e933f123c663bc33a6b424343cf421942673e54391540214102433b4371417b432b3f873d1742e1427741353c763dde440344c2408e3dc73da1446144c83f653f93417c419544d34030456a458441f53cca3e9443aa464b469f437a42c83f8343d63e0c42373c3f41763f0d3e743f2a407a3d60419e40d83e633f88433a44f141eb446c456a40dc4074424443a1410641953d1e42e741503d9444d243ef414242e73d5b3b85409b42d4415f40a4416141903ee23fd2419943d242bc3fd43cbc4226455e40de3ee04002421e428a3e5a40014428418640c341c13e6d414e4198411b3e2f3d903d3039463fcc415e3fbc40dc403b41a64236427f3c9439583c7d3a5c3ad73dcc37bf37913a8e3aa23bc9423d4c7a54f15abd65d66add6d6d6dd56806000000180000009b0e0000581d0000c6490100ba0e000003000000040000000f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f020104030605080704000002a002000042160a0068060000d15ac6ec040000000300000000000000010000003400000004000900030000008e06ceff2c000000fd0000006d0022f0590f000003000000db04bdff00000000fd0000004e00cc0d3c11000002000000000200007c7ba886218b178de0895086fc7c2b77d2702769855f55594d532d500847e44308461452025feb66bd65615cd3584b566b50e5506f51e64ea64b0c49114697461d44ec419e47854fa953d850eb4a75461d42a73f4f4716475346f047d3437c488e47e9410c411f415441c13f9d409a3fa9421446c448e6421444a046db44fc44e245284329438a424b414845a0408d40d63bb84447415e3f7042843fc03b603dd140403e983c1840af433044244318449c43a9428b40b33dd5404a40b03edc419441ac3e723ee63ef642fe42ee42dd3e3041453ecf3dcc3b35405d3ec44054422e446d3fbf4268444440ee3db443f5400000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
//        String hexString = "020104030605080704000002a002000042160a005e000000a917b0dc040000000300000000000000010000003400000004000900030000003607ceff2c000000fd000000710022f0590f0000030000001a05bdff00000000fd0000005b007d0ea81000000200000000020000737aaf86e98b148e1a8aa985f57d5a76406c93676a5fb858af52264e8a49704d504c3a4a515ec966fa65bc5c1055855443513f5191508b4dbe4cf142d544d243964a4e44784dd7568d580253464cec4a9946db4a555040533653024ca84af0450c44e045c03f4e4399442e469345654266440742b843c543d541cd3e22456342003faa44f544e740ca428946d2440f43a040ac423a43a64351462b435c4310429941ef415944c144ec4241446b4189405c42fb40c03e03402040b93ff53f773feb3efe3ddb3c563f0544ce45994230403c41f540c33f5e423f41e73f0d3b573f8742c745f044bb421e43e43e053ee44221416c44cd44044235445143ae3dc53cb840013d0240a93e623db3435d42a540bc417443e03e5942a442e03e0b424741174400459a462e4733473b44cb40903e6d3d3143614394410842643d31432a433242ac3ee742af440c42fa415340c23fad408e415f41cd414943e541103dd03c2540e73f56403d42d33fb743983b043c9f3ae641284507423a408c44dc406943b83fe1404f3e9d3f33363d4360438142e543593d8641673d593fb84036422544fe433141fb3e0a42583e21405a4148406d3f05422c42214277411a42de3fd43f013ef93e193e144015410543cb426b3ff63c043a233e093ce83da73c6e3ac13874397f34eb3335365931bd3c8b3f6746c848a351b25cf5640b6b836e536e656606000000180000009c0e0000581d0000c5490100ba0e000003000000040000000f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
//        String hexString = "020104030605080704000002a002000042160a005e000000a917b0dc040000000300000000000000010000003400000004000900030000003607ceff2c000000fd000000710022f0590f0000030000001a05bdff00000000fd0000005b007d0ea81000000200000000020000737aaf86e98b148e1a8aa985f57d5a76406c93676a5fb858af52264e8a49704d504c3a4a515ec966fa65bc5c1055855443513f5191508b4dbe4cf142d544d243964a4e44784dd7568d580253464cec4a9946db4a555040533653024ca84af0450c44e045c03f4e4399442e469345654266440742b843c543d541cd3e22456342003faa44f544e740ca428946d2440f43a040ac423a43a64351462b435c4310429941ef415944c144ec4241446b4189405c42fb40c03e03402040b93ff53f773feb3efe3ddb3c563f0544ce45994230403c41f540c33f5e423f41e73f0d3b573f8742c745f044bb421e43e43e053ee44221416c44cd44044235445143ae3dc53cb840013d0240a93e623db3435d42a540bc417443e03e5942a442e03e0b424741174400459a462e4733473b44cb40903e6d3d3143614394410842643d31432a433242ac3ee742af440c42fa415340c23fad408e415f41cd414943e541103dd03c2540e73f56403d42d33fb743983b043c9f3ae641284507423a408c44dc406943b83fe1404f3e9d3f33363d4360438142e543593d8641673d593fb84036422544fe433141fb3e0a42583e21405a4148406d3f05422c42214277411a42de3fd43f013ef93e193e144015410543cb426b3ff63c043a233e093ce83da73c6e3ac13874397f34eb3335365931bd3c8b3f6746c848a351b25cf5640b6b836e536e656606000000180000009c0e0000581d0000c5490100ba0e000003000000040000000f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        String hexString = "020104030605080704000002c002000042160a000c080000db411ffb060000000300000000000000010000004c00000006000900020000003b061f00200000001e0000004702c6fe5002000040000000bf0035fdd6040000fe0000006c006513bb0a000002000000b905d8ff13000000fe000000520061efa90e00000200000000020000487adb881b904e8eb48890858f8150771877b374dc78a0793a796f7af776fe733e6f6173f070d66c9d6c706707645c607563db6ac670ab789a81c884cb843f7f5b76cd6a035e56580c5b7b5dd45d50555d53df5786598a599c589d5876596a5291531f559654eb58ec5e675feb5e475de86bc271d76e656a695ee659815f476e19750c728e6d846af0614e5ac2514457fd577b51394b6c52cf4f654fc84fbd55fc5aa657bd55105afb5e915dfc544b58f0532550cc4b59477c51cb50364a8b4aaf4c6751a555dd548656c64e744f11540951014be84bea50344d1f4c314fdf4b8143fa48614a284b3148204d8e490f49d74a734f505287501249b64c1c49da44ce417e42ff422c450745df40d83f07441b43cb423c45cb46e44a3d49d1457645c53fc640b5414f44b044ee4497405b416142bb405c408240fb446741e445ca4a9f49c94d2d4fba4a9642cf417245d7450f49a649bf4627444e43fc44f3462c40593e973e49419f416a43123f07449346e74073405b421c442d43654120422d461046bf42ed42fd4176427441e04445425b426242c9410141bc422e44653b6b4400457d45cc4376422c41cb4450438b3ebf3fa03d854330419241b23df3440a42f741cc3f763fbe3fbb4155415f3dea3f9240303ff141a141663d5539ad38233d553d004275460a4b294b9b4f91513851bf510d54c65f8f648369816f3a711b6c0600000018000000b80e0000be1e0000a8490100ba0e000003000000050000000f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f02010403";
//        String target = hexString.substring(56, 64);
        String length = hexString.substring(24, 32);
        System.out.println(length);

        long target_count = processor.hexstring2Uint32(length);

        System.out.println(target_count);

        List a = processor.on_V2Data(hexString);

        String dataString = hexString.substring(80);
        processor.on_V2Target_Data(dataString, (long)a.get(1), (String)a.get(2));
        System.out.println("j");
    }


    @Test
    public void littleEndianString2HexString(){
        DataProcessor processor = new DataProcessor();
        String s = processor.littleEndianString2HexString("1234");
        System.out.println(s);

    }

    @Test
    public void hexString2BinaryString() throws IOException {
        DataProcessor processor = new DataProcessor();
        String s = processor.hexstring2BinaryString("1234", false);
        System.out.println(s);
    }

    @Test
    public void hexString2QFromat() throws IOException {
        DataProcessor processor = new DataProcessor();
        double i = processor.qformatString2Unit16("8142", 0);
        System.out.println(i);
    }


}
