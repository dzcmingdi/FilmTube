import Vue from 'vue';
import vuetify from "../plugins/vuetify";
import WebUploader from 'webuploader';
import video_dialog from "../components/video-dialog.vue";

let _ = require("lodash")
new Vue({
    el: '#upload',
    vuetify,
    mounted() {
        this.uploader = new WebUploader.Uploader({
            server: "/video/upload/complete",
            chunked: true,
            chunkSize: 1024 * 1024 * 10
        })
    },
    components: {
        'video-dialog': video_dialog
    },
    data: {
        uploader: undefined,
        waitFileList: [],
        processingList: [],
        preparedFileList: [],
        addFilesButtonDisabled: true,
        selectedFilesHint: false,
        dialog: false,
        uploadVideoDialogShow: false,
        number: undefined,
        videoType: undefined
    },
    computed: {},
    methods: {
        dialogShow: function (args) {
            switch (args.option) {
                case 'close':
                    this.uploadVideoDialogShow = false;
                    return

                case 'upload':
                    let processingList = this.processingList;
                    let preparedList = this.preparedFileList;

                    processingList[this.number].videoId = args.id;
                    processingList[this.number].kind = args.kind;
                    processingList[this.number].episode = args.episode;
                    processingList[this.number].complete = 0;
                    this.$set(preparedList, preparedList.length, processingList[args.number]);
                    this.uploader.addFile(processingList[args.number]);
                    this.$delete(processingList, args.number);
                    this.uploadVideoDialogShow = false
                    return
                case 'open':
                    this.uploadVideoDialogShow = true;
                    return
            }
        },
        uploadInputOnChange: function (e) {
            _.remove(e, (element, i) => {
                element.progress = 0;
                element.complete = 0;
                switch (element.type) {
                    // case 'video/x-matroska':
                    //     this.selectedFilesHint = false;
                    //     return false;
                    case 'video/mp4':
                        this.selectedFilesHint = false;
                        return false;
                    default:
                        this.selectedFilesHint = true;
                        return true;
                }
            });
            this.waitFileList = [];
            this.waitFileList = this.waitFileList.concat(e);
            this.addFilesButtonDisabled = this.waitFileList.length === 0;

        },
        addFilesToProcessingList: function () {
            for (let i = 0; i < this.waitFileList.length; i++) {
                this.waitFileList[i].number = this.preparedFileList.length;
                this.processingList.push(this.waitFileList[i]);
            }
            if (this.waitFileList.length !== 0) {
                // this.uploader.addFiles(this.waitFileList);
                this.waitFileList = [];
                this.selectedFilesHint = false;
            }
            /* TODO 需要在此清空文件输入框内容*/

        },
        parseWebUploaderFileId: function (str) {
            let pattern = /^.*_(\d+)$/;
            return parseInt(str.match(pattern)[1]);
        },
        uploadFilesToServer: function () {
            this.uploader.upload();
            let beforeUploaded = function (block, data) {
                data.id = block.file.source.source.videoId;
                data.kind = block.file.source.source.kind;
                data.episode = block.file.source.source.episode;
            }.bind(this);
            let uploadProgress = function (file, percentage) {
                let number = this.parseWebUploaderFileId(file.id);
                let preparedFile = this.preparedFileList[number];
                preparedFile.progress = percentage * 100;
                this.$set(this.preparedFileList, number, preparedFile);
            }.bind(this);
            let uploadSuccess = function (file, response) {
                let number = this.parseWebUploaderFileId(file.id);
                if (response['code'] !== 0) {
                    return;
                }
                let preparedFile = this.preparedFileList[number];
                // preparedFile.id = response['data']['videoId'];
                preparedFile.complete = 1;
                this.$set(this.preparedFileList, number, preparedFile);
            }.bind(this);

            this.uploader.on('uploadProgress', uploadProgress);
            this.uploader.on('uploadSuccess', uploadSuccess);
            this.uploader.on('uploadBeforeSend', beforeUploaded);
        },
        uploadVideoInfo: function (number, videoType) {
            this.uploadVideoDialogShow = true;
            this.number = number;
            this.videoType = videoType;
        }
    }
})