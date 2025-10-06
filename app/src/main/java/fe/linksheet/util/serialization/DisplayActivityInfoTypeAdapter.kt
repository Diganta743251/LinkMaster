package fe.linksheet.util.serialization

import android.net.Uri
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter


object UriTypeAdapter : TypeAdapter<Uri>() {
    override fun read(reader: JsonReader): Uri {
        return Uri.parse(reader.nextString())
    }

    override fun write(out: JsonWriter, value: Uri) {
        out.value(value.toString())
    }
}

// HttpUrl adapter removed to avoid okhttp dependency

//class DisplayActivityInfoListSerializer(val resolveInfos: Map<String, Pair<ResolveInfo, String>>) :
//    JsonSerializer<List<DisplayActivityInfo>>, JsonDeserializer<List<DisplayActivityInfo>> {
//    override fun serialize(
//        src: List<DisplayActivityInfo>,
//        typeOfSrc: Type,
//        context: JsonSerializationContext?,
//    ): JsonElement {
//        val packages = src.map { it.resolvedInfo.activityInfo.packageName }
//        return jsonArray(packages)
//    }
//
//    override fun deserialize(
//        json: JsonElement,
//        typeOfT: Type,
//        context: JsonDeserializationContext?,
//    ): List<DisplayActivityInfo> {
//        val arr = json.array()
//        return arr.map { obj ->
//            val (resolveInfo, label) = resolveInfos[obj.`object`().asString("resolvedInfo")]!!
//            DisplayActivityInfo(resolveInfo, label)
//        }
//    }
//
//}
//
//class DisplayActivityInfoSerializer(
//    val resolveInfos: Map<String, Pair<ResolveInfo, String>>,
//) : JsonSerializer<DisplayActivityInfo>, JsonDeserializer<DisplayActivityInfo> {
//    override fun serialize(src: DisplayActivityInfo, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
//        return jsonObject {
//            "resolvedInfo" += src.resolvedInfo.activityInfo.packageName
//            "label" += src.label
//        }
//    }
//
//    override fun deserialize(
//        json: JsonElement,
//        typeOfT: Type,
//        context: JsonDeserializationContext,
//    ): DisplayActivityInfo {
//        val obj = json.`object`()
//        val (resolveInfo, label) = resolveInfos[obj.asString("resolvedInfo")]!!
//
//        return DisplayActivityInfo(resolveInfo, label)
//    }
//}
