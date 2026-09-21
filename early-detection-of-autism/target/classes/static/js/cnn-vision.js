/**
 * CNN VISUAL FEATURE EXTRACTOR
 * Uses TensorFlow.js + MobileNet (a pretrained CNN) locally in the browser.
 * It creates a visual embedding from sampled camera frames. It is NOT an
 * autism classifier. A research-grade autism model requires ethically
 * collected, labelled child data and clinical validation.
 */
class CNNVisualFeatureExtractor {
  constructor(videoId="webcamVideo"){this.video=document.getElementById(videoId);this.model=null;this.ready=false;this.samples=0;this.meanActivation=0;}
  async init(){
    try{
      if(!window.tf||!window.mobilenet) return false;
      this.model=await mobilenet.load({version:2,alpha:0.5});
      this.ready=true; this.sample();
      return true;
    }catch(e){console.warn("CNN feature extractor unavailable:",e);return false;}
  }
  async sample(){
    if(!this.ready||!this.video||this.video.readyState<2)return;
    try{
      const embedding=this.model.infer(this.video,true);
      const data=await embedding.data();
      let sum=0; for(const v of data)sum+=Math.abs(v);
      this.meanActivation=sum/data.length;
      this.samples++;
      embedding.dispose?.();
      window.cnnVision={ready:true,samples:this.samples,meanActivation:Number(this.meanActivation.toFixed(5))};
    }catch(e){console.warn("CNN sampling skipped:",e);}
    setTimeout(()=>this.sample(),2500);
  }
}
window.CNNVisualFeatureExtractor=CNNVisualFeatureExtractor;
