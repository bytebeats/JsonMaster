package me.bytebeats.jsonmaster.util

import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/14 15:28
 * @Version 1.0
 * @Description TreeModelFactory creates TreeModel and its TreeNodes .
 */

object TreeModelFactory {
    fun create(json: String): DefaultTreeModel {
        val root = DefaultMutableTreeNode("")
        val lines = json.split("\n".toRegex())
        val iterator = lines.listIterator()
        createNode(iterator, root)
        return DefaultTreeModel(root)
    }

    private fun createNode(iterator: ListIterator<String>, root: DefaultMutableTreeNode): DefaultMutableTreeNode {
        while (iterator.hasNext()) {
            var line = iterator.next().trim()
            if (line.contains('[') || line.contains('{')) {
//                line.contains("[\\[\\{]".toRegex())
                val node = DefaultMutableTreeNode(line)
                root.add(createNode(iterator, node))
            } else {
                var breakable = true
                while (true) {
                    val node = DefaultMutableTreeNode(line)
                    if (line.contains(']') || line.contains('}')) {
//                        line.contains("[\\]\\}]".toRegex())
                        root.add(node)
                        break
                    } else if (line.contains('[') || line.contains('{')) {
//                        line.contains("[\\[\\{]".toRegex())
                        iterator.previous()
                        breakable = false
                        break
                    } else {
                        root.add(node)
                        if (iterator.hasNext()) {
                            line = iterator.next().trim()
                        } else {
                            break
                        }
                    }
                }
                if (breakable) {
                    break
                }
            }
        }
        return root
    }
}