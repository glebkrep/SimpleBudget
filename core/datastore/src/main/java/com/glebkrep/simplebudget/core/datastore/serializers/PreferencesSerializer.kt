package com.glebkrep.simplebudget.core.datastore.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.glebkrep.simplebudget.PreferencesProto
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class PreferencesSerializer @Inject constructor() : Serializer<PreferencesProto> {
    override val defaultValue: PreferencesProto
        get() = PreferencesProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): PreferencesProto {
        try {
            return PreferencesProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: PreferencesProto, output: OutputStream) {
        t.writeTo(output)
    }
}
