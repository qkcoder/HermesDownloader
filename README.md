# HermesDownloader

HermesDownloader is a suitable for the Android platform of the top speed downloader.



## Start a download task

```Kotlin
HermesDownloader.getInstance().startDownloadTask(
  "fileKey",
  "fileUrl",
  object : HermesDownloaderListener {
          override fun onSuccess(fileKey: String,
                                 cachePath: String,
                                 errorCode: Int) {
            
          }
    
          override fun onFailed(fileKey: String, errorCode: Int) {
          }
          
          override fun onPause(fileKey: String) {
          }
    
          override fun onCancel(fileKey: String) {
          }
          
          override fun onProgress(fileKey: String,
                                  downloadLength: Long,
                                  totalLength: Long ) {
          }
  }
)
```



## Releases

The lateset release is available on [Maven Central](https://search.maven.org/).

```groovy
implementation 'io.github.qkcoder:HermesDownloader:0.0.1'
```





## License

```
Copyright 2021 qkcoder.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```