package me.bytebeats.jsonmstr.util

import com.fasterxml.jackson.databind.JsonNode

object CsvUtil {
    fun hasNestedObjects(node: JsonNode): Boolean {//recursive function to detect JsonNode has child node of object or array
        return if (node.isObject) {
            val children = node.elements()
            while (children.hasNext()) {
                if (children.next().isContainerNode) {
                    return true
                }
            }
            false
        } else if (node.isArray) {
            for (i in 0 until node.size()) {
                if (hasNestedObjects(node[i])) {
                    return true
                }
            }
            false
        } else {
            false
        }
    }

    fun parseCSV(
        columns: MutableList<String>,
        values: MutableList<String>,
        node: JsonNode,
        prefix: String
    ) {//generate columns and its Corresponding value in csv file
        if (node.isObject) {
            node.fieldNames().forEachRemaining { fieldName: String ->
                if (node[fieldName].isValueNode) {
                    columns.add(prefix + fieldName)
                    values.add(node[fieldName].asText())
                } else {
                    parseCSV(columns, values, node[fieldName], "$prefix$fieldName.")
                }
            }
        } else {
            val children = node.elements()
            var count = 0
            while (children.hasNext()) {
                parseCSV(columns, values, children.next(), prefix + count++ + ".")
            }
        }
    }
}