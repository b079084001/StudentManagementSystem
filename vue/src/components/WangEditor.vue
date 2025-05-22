<template>
  <div style="border: 1px solid #EEEFF0;border-radius: 5px;overflow: hidden;width: 100%;">
    <Toolbar
        :editor="editorRef"
        :defaultConfig="toolbarConfig"
        :mode="mode"
        style="border-bottom: 1px solid #ccc"
    />
    <Editor
        :defaultConfig="editorConfig"
        :mode="mode"
        v-model="valueHtml"
        style="height: 500px; overflow-y: hidden;"
        @onCreated="handleCreated"
        @onDestroyed="handleDestroyed"
    />
  </div>

</template>
<script setup>
import '@wangeditor/editor/dist/css/style.css';
import {defineComponent, onBeforeUnmount, ref, shallowRef} from "vue";
import {Editor, Toolbar} from "@wangeditor/editor-for-vue";

const mode = 'default'
const editorRef = shallowRef()
const valueHtml = ref('')
const toolbarConfig = {}
const editorConfig = {
  placeholder: '请输入内容...',
  MENU_CONF: {
    uploadImage: {
      fieldName: 'file',
      server: `${import.meta.env.VITE_BASE_URL}/commons/upload`
    }
  }
}

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor != null) return
  editor.destroy()
})

const handleCreated = (editor) => {
  editorRef.value = editor // 记录 editor 实例，重要！
}
const handleDestroyed = (editor) => {

}
</script>