package com.glebkrep.simplebudget.core.datastore.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.glebkrep.simplebudget.BudgetDataProto
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class BudgetDataSerializer @Inject constructor() : Serializer<BudgetDataProto> {
    override val defaultValue: BudgetDataProto
        get() = BudgetDataProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): BudgetDataProto {
        try {
            return BudgetDataProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: BudgetDataProto, output: OutputStream) {
        t.writeTo(output)
    }
}
