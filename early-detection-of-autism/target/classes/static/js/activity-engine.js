/**
 * PIP'S PLAY WORLD ACTIVITY ENGINE
 * Modern, colorful, highly engaging games designed specifically for young children (ages 2-6).
 *
 * Multimodal Data Flow:
 * Child Interaction -> Camera (Gaze/Motion) + Voice (Audio/Speech) + CNN Feature Extractor
 * -> Multimodal Feature Fusion -> Random Forest Inference -> Parent/Clinician Dashboard.
 */

class ActivityEngine {
  constructor() {
    this.currentStage = 2;
    this.sessionId = 1;
    this.childId = 1;
    this.childName = "Superstar";
    this.language = "en";

    this.board = document.getElementById("gameBoard");
    this.next = document.getElementById("btnNextStage");
    this.title = document.getElementById("stageTitle");
    this.instruction = document.getElementById("gameInstruction");
    this.pill = document.getElementById("stagePill");
    this.fill = document.getElementById("progressFill");
    this.art = document.getElementById("gameArt");

    this.feedback = null;
    this.interactionCount = 0;
    this.stageStartTime = 0;
    this.stageTimer = null;
    this.gameDone = false;
    this.sequenceIndex = 0;

    this.mascot = new MascotGuide();
    this.cameraSensor = new CameraVisionSensor();
    this.voiceSensor = new VoiceProcessorSensor();
    this.cnnExtractor = new CNNVisualFeatureExtractor('webcamVideo');

    this.confettiCanvas = document.getElementById("confettiCanvas");
    this.confettiCtx = this.confettiCanvas ? this.confettiCanvas.getContext("2d") : null;
    this.confettiParticles = [];
    this.confettiRunning = false;
    this.mediaStream = null;
    this.sensorStatus = document.getElementById("sensorStatus");
    this.enableSensorsButton = document.getElementById("btnEnableSensors");

    this.bindUI();
  }

  async init() {
    const params = new URLSearchParams(window.location.search);
    if (params.has("sessionId")) this.sessionId = Number(params.get("sessionId"));
    if (params.has("childId")) this.childId = Number(params.get("childId"));
    if (params.has("name")) this.childName = params.get("name");
    if (params.has("lang")) {
      this.language = params.get("lang");
      this.mascot.setLanguage(this.language);
      this.voiceSensor.setLanguage(this.language === "ml" ? "ml-IN" : "en-US");
    }

    // Request both devices in one browser permission prompt and share the stream.
    this.enableHardwareSensors();

    // Initialize CNN visual feature extractor
    try {
      this.cnnExtractor.init();
    } catch (e) {
      console.warn("CNN vision layer initialized in passive mode");
    }

    this.resizeCanvas();
    window.addEventListener("resize", () => this.resizeCanvas());

    // Load initial stage (Stage 2: Bubble Garden)
    this.loadStage(2);
  }

  resizeCanvas() {
    const c = document.getElementById("trackingCanvas");
    const v = document.getElementById("webcamVideo");
    if (c && v) {
      c.width = v.clientWidth || 320;
      c.height = v.clientHeight || 190;
    }
    if (this.confettiCanvas) {
      this.confettiCanvas.width = window.innerWidth;
      this.confettiCanvas.height = window.innerHeight;
    }
  }

  bindUI() {
    this.next?.addEventListener("click", () => this.advanceToNextStage());
    this.enableSensorsButton?.addEventListener("click", () => this.enableHardwareSensors());
  }

  async enableHardwareSensors() {
    if (!navigator.mediaDevices?.getUserMedia) {
      this.updateSensorStatus("This browser does not support camera and microphone access.", false);
      return false;
    }

    this.updateSensorStatus("Requesting camera + microphone permission…", false);
    try {
      if (!this.mediaStream || this.mediaStream.getTracks().some(track => track.readyState === "ended")) {
        this.mediaStream = await navigator.mediaDevices.getUserMedia({
          video: { width: { ideal: 640 }, height: { ideal: 480 }, facingMode: "user" },
          audio: { echoCancellation: true, noiseSuppression: true, autoGainControl: true }
        });
      }

      const cameraReady = await this.cameraSensor.startCamera(this.mediaStream);
      this.voiceSensor.attachStream(this.mediaStream);
      const audioReady = this.mediaStream.getAudioTracks().some(track => track.readyState === "live");
      if (!cameraReady || !audioReady) {
        throw new Error("One or more media tracks are unavailable.");
      }

      this.updateSensorStatus("Camera + microphone active • Live features stay on this device.", true);
      if (this.enableSensorsButton) this.enableSensorsButton.hidden = true;
      return true;
    } catch (error) {
      console.warn("Combined camera and microphone access was not granted:", error);
      this.updateSensorStatus("Camera + microphone access is needed for live observations. You can try again.", false);
      if (this.enableSensorsButton) this.enableSensorsButton.hidden = false;
      return false;
    }
  }

  updateSensorStatus(message, active) {
    if (this.sensorStatus) {
      this.sensorStatus.textContent = message;
      this.sensorStatus.dataset.active = active ? "true" : "false";
    }
  }

  loadStage(stage) {
    if (this.stageTimer) clearInterval(this.stageTimer);
    this.currentStage = stage;
    this.interactionCount = 0;
    this.gameDone = false;
    this.sequenceIndex = 0;
    this.stageStartTime = performance.now();
    this.cameraSensor.resetMetrics();
    this.voiceSensor.resetMetrics();

    const gameIndex = stage - 1;
    if (this.pill) this.pill.textContent = `Game ${gameIndex} of 8`;
    if (this.fill) this.fill.style.width = `${(gameIndex / 8) * 100}%`;

    const stageConfigs = {
      2: ["Bubble Garden", "Tap each bubble to pop it and grow friendly flowers! 🌸", "assets/garden.svg"],
      3: ["Rainbow Hunt", "Pip needs your sharp eyes! Tap the magical color. 🎯", "assets/rainbow.svg"],
      4: ["Animal Friends", "Choose your favorite animal and make its happy sound! 🐾", "assets/animals.svg"],
      5: ["Copy Pip", "Look at the cheerful pose and try your best copy! ⭐", "assets/rocket.svg"],
      6: ["Say Hello", "Press the microphone and say “hello” to Pip! 🎤", "assets/animals.svg"],
      7: ["Happy Faces", "Which picture shows Pip's super-duper happy smile? 😊", "assets/faces.svg"],
      8: ["Build a Rocket", "Tap the colored pieces in order: Blue ➔ Pink ➔ Green! 🚀", "assets/blocks.svg"],
      9: ["Star Rhythm", "Tap all 4 musical stars to make a triumphant song! ✨", "assets/rocket.svg"]
    };

    const cfg = stageConfigs[stage] || stageConfigs[2];
    if (this.title) this.title.textContent = cfg[0];
    if (this.instruction) this.instruction.textContent = cfg[1];
    if (this.art) this.art.src = cfg[2];

    this.mascot.speak(cfg[1]);

    if (this.next) {
      this.next.disabled = false;
      this.next.textContent = stage === 9 ? "View Parent Report 📊" : "Next Game ➜";
    }

    this.renderStage(stage);
  }

  setupBoardBase() {
    this.board.innerHTML = "";
    const prompt = document.createElement("div");
    prompt.className = "big-instruction";
    prompt.textContent = this.instruction.textContent;
    this.board.appendChild(prompt);

    this.feedback = document.createElement("div");
    this.feedback.className = "feedback";
    this.board.appendChild(this.feedback);
  }

  setFeedback(text, speak = true) {
    if (this.feedback) {
      this.feedback.textContent = text;
    }
    if (speak) {
      this.mascot.speak(text);
    }
  }

  renderStage(stage) {
    this.setupBoardBase();

    if (stage === 2) {
      // ----------------------------------------------------------------------
      // GAME 1: BUBBLE GARDEN
      // ----------------------------------------------------------------------
      const grid = document.createElement("div");
      grid.className = "bubble-garden-container";
      this.board.insertBefore(grid, this.feedback);

      const flowers = ["🌸", "🌻", "🌷", "🌼", "🌺", "🌹"];
      let poppedCount = 0;

      for (let i = 0; i < 6; i++) {
        const btn = document.createElement("button");
        btn.className = "bubble-button";
        btn.innerHTML = `<span aria-hidden="true">🫧</span>`;
        btn.setAttribute("aria-label", "Pop bubble");

        btn.onclick = () => {
          this.interactionCount++;
          this.mascot.playTone("pop");
          btn.innerHTML = `<span class="flower-bloom">${flowers[i % flowers.length]}</span>`;
          btn.style.pointerEvents = "none";
          btn.style.animation = "none";
          poppedCount++;

          if (poppedCount < 6) {
            this.setFeedback(`Pop! A pretty flower bloomed! (${poppedCount}/6) 🌱`, false);
          } else {
            this.gameDone = true;
            this.mascot.playTone("fanfare");
            this.triggerConfetti();
            this.setFeedback("Superstar! You grew the most beautiful garden! 🌸✨", true);
          }
        };

        grid.appendChild(btn);
      }

    } else if (stage === 3) {
      // ----------------------------------------------------------------------
      // GAME 2: RAINBOW HUNT
      // ----------------------------------------------------------------------
      const palette = [
        { name: "Blue", hex: "#3b82f6" },
        { name: "Sunny Yellow", hex: "#eab308" },
        { name: "Candy Pink", hex: "#ec4899" },
        { name: "Emerald Green", hex: "#10b981" }
      ];

      const targetColor = palette[Math.floor(Math.random() * palette.length)];
      this.instruction.textContent = `Can you tap the ${targetColor.name} circle? 🎯`;
      this.mascot.speak(this.instruction.textContent);

      const row = document.createElement("div");
      row.className = "rainbow-grid";
      this.board.insertBefore(row, this.feedback);

      palette.forEach(item => {
        const btn = document.createElement("button");
        btn.className = "color-jelly-btn";
        btn.style.backgroundColor = item.hex;
        btn.setAttribute("aria-label", item.name);
        btn.title = item.name;

        btn.onclick = () => {
          this.interactionCount++;
          if (item.name === targetColor.name) {
            this.gameDone = true;
            this.mascot.playTone("chime");
            btn.style.transform = "scale(1.2)";
            btn.style.boxShadow = `0 0 25px ${item.hex}`;
            this.triggerConfetti();
            this.setFeedback(`Hooray! That is the ${targetColor.name} one! 🌈`, true);
          } else {
            this.mascot.playTone("pop");
            this.setFeedback(`Nice try! Pip is looking for the ${targetColor.name} one. Try another! 💛`, false);
          }
        };

        row.appendChild(btn);
      });

    } else if (stage === 4) {
      // ----------------------------------------------------------------------
      // GAME 3: ANIMAL FRIENDS
      // ----------------------------------------------------------------------
      const animals = [
        { name: "Puppy Dog", emoji: "🐶", tone: "woof", soundText: "Woof Woof!" },
        { name: "Playful Kitty", emoji: "🐱", tone: "meow", soundText: "Meow Meow!" },
        { name: "Bouncy Bunny", emoji: "🐰", tone: "chirp", soundText: "Hop Hop!" }
      ];

      const grid = document.createElement("div");
      grid.className = "animal-grid";
      this.board.insertBefore(grid, this.feedback);

      animals.forEach(a => {
        const card = document.createElement("button");
        card.className = "animal-card";
        card.innerHTML = `
          <div class="animal-emoji">${a.emoji}</div>
          <div class="animal-name">${a.name}</div>
        `;

        card.onclick = () => {
          this.interactionCount++;
          this.gameDone = true;
          this.mascot.playTone(a.tone);
          this.voiceSensor.startListening(a.name.toLowerCase());
          this.setFeedback(`The ${a.name} says ${a.soundText}! Can you say hello back? 🎉`, true);
        };

        grid.appendChild(card);
      });

    } else if (stage === 5) {
      // ----------------------------------------------------------------------
      // GAME 4: COPY PIP (POSE IMITATION)
      // ----------------------------------------------------------------------
      const card = document.createElement("div");
      card.className = "action-card";
      card.innerHTML = `
        <div class="action-emoji">🙆</div>
        <div class="action-desc">Big Stretch to the Sky! Reach both arms up high! ✨</div>
        <button id="btnPoseDone" class="btn-child btn-purple" style="font-size: 1.25rem;">
          I'm Doing It! ⭐
        </button>
      `;
      this.board.insertBefore(card, this.feedback);

      card.querySelector("#btnPoseDone").onclick = () => {
        this.interactionCount++;
        this.cameraSensor.recordImitationPose("big_stretch");
        this.gameDone = true;
        this.mascot.playTone("success");
        this.triggerConfetti();
        this.setFeedback("Fantastic stretching! Pip gives you 5 golden stars! 🌟🌟🌟🌟🌟", true);
      };

    } else if (stage === 6) {
      // ----------------------------------------------------------------------
      // GAME 5: SAY HELLO (VOCAL & COMMUNICATION)
      // ----------------------------------------------------------------------
      const hub = document.createElement("div");
      hub.className = "voice-hub";
      hub.innerHTML = `
        <div class="mic-halo-button" id="micHaloBtn" role="button" aria-label="Microphone">
          <div class="mic-pulse-ring"></div>
          🎤
        </div>
        <div class="voice-equalizer">
          <div class="eq-bar"></div>
          <div class="eq-bar"></div>
          <div class="eq-bar"></div>
          <div class="eq-bar"></div>
          <div class="eq-bar"></div>
        </div>
        <button id="btnSpeakPrompt" class="btn-child btn-blue" style="font-size: 1.2rem;">
          🎤 Tap & Say “Hello”
        </button>
      `;
      this.board.insertBefore(hub, this.feedback);

      const activateMic = () => {
        this.interactionCount++;
        this.voiceSensor.startListening("hello");
        this.gameDone = true;
        this.mascot.playTone("chime");
        this.setFeedback("Listening… Say “Hello Pip” or any happy sound! 💛", false);

        setTimeout(() => {
          this.setFeedback("Pip heard your cheerful voice! High five! 🙌", true);
        }, 2200);
      };

      hub.querySelector("#micHaloBtn").onclick = activateMic;
      hub.querySelector("#btnSpeakPrompt").onclick = activateMic;

    } else if (stage === 7) {
      // ----------------------------------------------------------------------
      // GAME 6: HAPPY FACES
      // ----------------------------------------------------------------------
      const faces = [
        { emoji: "😊", label: "Happy", isCorrect: true },
        { emoji: "😮", label: "Surprised", isCorrect: false },
        { emoji: "😴", label: "Sleepy", isCorrect: false }
      ];

      const grid = document.createElement("div");
      grid.className = "face-grid";
      this.board.insertBefore(grid, this.feedback);

      faces.forEach(f => {
        const btn = document.createElement("button");
        btn.className = "face-btn";
        btn.innerHTML = `
          <span>${f.emoji}</span>
          <span class="face-label">${f.label}</span>
        `;

        btn.onclick = () => {
          this.interactionCount++;
          if (f.isCorrect) {
            this.gameDone = true;
            this.mascot.playTone("chime");
            this.triggerConfetti();
            btn.style.borderColor = "#10b981";
            btn.style.boxShadow = "0 0 25px rgba(16, 185, 129, 0.4)";
            this.setFeedback("Yes! That is Pip's super happy smile! Keep smiling! 😊✨", true);
          } else {
            this.mascot.playTone("pop");
            this.setFeedback(`That's a nice ${f.label} face! Can you find the Happy one? 😊`, false);
          }
        };

        grid.appendChild(btn);
      });

    } else if (stage === 8) {
      // ----------------------------------------------------------------------
      // GAME 7: BUILD A ROCKET (SEQUENCING PUZZLE)
      // ----------------------------------------------------------------------
      const arena = document.createElement("div");
      arena.className = "rocket-arena";

      const rocketBox = document.createElement("div");
      rocketBox.className = "rocket-silhouette-box";
      rocketBox.id = "rocketDisplay";
      rocketBox.textContent = "🪐";
      arena.appendChild(rocketBox);

      const partsRow = document.createElement("div");
      partsRow.className = "rocket-parts-row";

      const sequence = [
        { label: "1. Blue Base", emoji: "🟦", part: "base" },
        { label: "2. Pink Body", emoji: "🩷", part: "body" },
        { label: "3. Green Nose", emoji: "🟩", part: "nose" }
      ];

      this.sequenceIndex = 0;

      sequence.forEach((item, index) => {
        const pBtn = document.createElement("button");
        pBtn.className = "rocket-part-btn";
        pBtn.innerHTML = `
          <span>${item.emoji}</span>
          <span class="part-label">${item.label}</span>
        `;

        pBtn.onclick = () => {
          this.interactionCount++;
          if (index === this.sequenceIndex) {
            this.sequenceIndex++;
            this.mascot.playTone("pop");
            pBtn.style.opacity = "0.35";
            pBtn.disabled = true;

            if (this.sequenceIndex === 1) {
              rocketBox.textContent = "🛞";
              this.setFeedback("Great! Base assembled. Now tap the Pink Body! 🩷", false);
            } else if (this.sequenceIndex === 2) {
              rocketBox.textContent = "🛸";
              this.setFeedback("Awesome! Now tap the Green Nose! 🟩", false);
            } else if (this.sequenceIndex === 3) {
              rocketBox.textContent = "🚀";
              rocketBox.style.animation = "poseWiggle 0.6s infinite alternate";
              this.gameDone = true;
              this.mascot.playTone("rocket");
              this.triggerConfetti();
              this.setFeedback("3… 2… 1… BLAST OFF! Space Explorer Rocket Launched! 🚀✨", true);
            }
          } else {
            this.mascot.playTone("pop");
            this.setFeedback("Follow the order: 1. Blue Base ➔ 2. Pink Body ➔ 3. Green Nose! 🚀", false);
          }
        };

        partsRow.appendChild(pBtn);
      });

      arena.appendChild(partsRow);
      this.board.insertBefore(arena, this.feedback);

    } else if (stage === 9) {
      // ----------------------------------------------------------------------
      // GAME 8: STAR RHYTHM (MUSICAL HARMONY FINALE)
      // ----------------------------------------------------------------------
      const grid = document.createElement("div");
      grid.className = "stars-music-grid";
      this.board.insertBefore(grid, this.feedback);

      const notes = [
        { label: "Do", tone: "pop" },
        { label: "Re", tone: "chime" },
        { label: "Mi", tone: "sparkle" },
        { label: "Fa", tone: "fanfare" }
      ];

      let starsTapped = 0;

      notes.forEach((n, idx) => {
        const star = document.createElement("button");
        star.className = "music-star-btn";
        star.innerHTML = `<span aria-hidden="true">⭐</span>`;
        star.setAttribute("aria-label", `Musical Star ${idx + 1}`);

        star.onclick = () => {
          this.interactionCount++;
          star.classList.add("star-tapped");
          star.disabled = true;
          this.mascot.playTone(n.tone);
          starsTapped++;

          if (starsTapped < 4) {
            this.setFeedback(`Star ${starsTapped} of 4 collected! ✨`, false);
          } else {
            this.gameDone = true;
            this.triggerConfetti();
            this.mascot.playTone("fanfare");
            this.setFeedback("YOU DID IT! Superstar of Pip's Play World! 🎉🏆", true);
          }
        };

        grid.appendChild(star);
      });
    }
  }

  triggerConfetti() {
    if (!this.confettiCtx || !this.confettiCanvas) return;
    this.confettiParticles = [];
    const colors = ["#f43f5e", "#ec4899", "#a855f7", "#3b82f6", "#10b981", "#facc15", "#fb923c"];

    for (let i = 0; i < 75; i++) {
      this.confettiParticles.push({
        x: Math.random() * this.confettiCanvas.width,
        y: -10,
        r: Math.random() * 6 + 4,
        d: Math.random() * 60,
        color: colors[Math.floor(Math.random() * colors.length)],
        tilt: Math.floor(Math.random() * 10) - 10,
        tiltAngleInc: (Math.random() * 0.07) + 0.05,
        tiltAngle: 0
      });
    }

    if (!this.confettiRunning) {
      this.confettiRunning = true;
      this.drawConfetti();
    }
  }

  drawConfetti() {
    if (!this.confettiCtx || !this.confettiCanvas) return;
    this.confettiCtx.clearRect(0, 0, this.confettiCanvas.width, this.confettiCanvas.height);

    let activeCount = 0;
    for (let i = 0; i < this.confettiParticles.length; i++) {
      const p = this.confettiParticles[i];
      p.tiltAngle += p.tiltAngleInc;
      p.y += (Math.cos(p.d) + 3 + p.r / 2);
      p.x += Math.sin(p.d);
      p.tilt = Math.sin(p.tiltAngle) * 15;

      if (p.y < this.confettiCanvas.height + 20) {
        activeCount++;
        this.confettiCtx.beginPath();
        this.confettiCtx.lineWidth = p.r;
        this.confettiCtx.strokeStyle = p.color;
        this.confettiCtx.moveTo(p.x + p.tilt + p.r / 2, p.y);
        this.confettiCtx.lineTo(p.x + p.tilt, p.y + p.tilt + p.r / 2);
        this.confettiCtx.stroke();
      }
    }

    if (activeCount > 0) {
      requestAnimationFrame(() => this.drawConfetti());
    } else {
      this.confettiRunning = false;
      this.confettiCtx.clearRect(0, 0, this.confettiCanvas.width, this.confettiCanvas.height);
    }
  }

  async advanceToNextStage() {
    if (this.stageTimer) clearInterval(this.stageTimer);
    const duration = Math.max(5, Math.round((performance.now() - this.stageStartTime) / 1000));

    try {
      // 1. Record activity record
      const activityResponse = await fetch(apiUrl(`/api/sessions/${this.sessionId}/activity`), {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          stageNumber: this.currentStage,
          stageName: `CHILD_GAME_${this.currentStage}`,
          durationSeconds: duration,
          interactionCount: this.interactionCount
        })
      });
      if (!activityResponse.ok) throw new Error("Activity could not be saved.");

      // 2. Transmit camera features
      const cf = this.cameraSensor.getExtractedFeatures(this.currentStage);
      cf.sessionId = this.sessionId;
      const cameraResponse = await fetch(apiUrl("/api/features/camera"), {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(cf)
      });
      if (!cameraResponse.ok) throw new Error("Camera features could not be saved.");

      // 3. Transmit voice features for vocal stages
      if (this.currentStage === 4 || this.currentStage === 6) {
        this.voiceSensor.stopListening();
        const vf = this.voiceSensor.getExtractedFeatures(this.currentStage);
        vf.sessionId = this.sessionId;
        const voiceResponse = await fetch(apiUrl("/api/features/voice"), {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(vf)
        });
        if (!voiceResponse.ok) throw new Error("Voice features could not be saved.");
      }
    } catch (err) {
      console.error("Backend feature sync failed:", err);
      this.setFeedback("We could not save this game's observations. Please check the connection and try again.", false);
      return;
    }

    if (this.currentStage < 9) {
      this.loadStage(this.currentStage + 1);
      window.scrollTo({ top: 0, behavior: "smooth" });
    } else {
      await this.completeSession();
    }
  }

  async completeSession() {
    this.cameraSensor.stopCamera();
    this.voiceSensor.stopListening();
    try {
      const response = await fetch(apiUrl(`/api/sessions/${this.sessionId}/complete`), { method: "POST" });
      if (!response.ok) throw new Error("Session completion failed.");
    } catch (e) {
      console.error("Session completion call:", e);
      this.setFeedback("Your observations could not be finalized. Please try again.", false);
      return;
    }
    window.location.href = `completion.html?sessionId=${this.sessionId}&childId=${this.childId}`;
  }
}

window.ActivityEngine = ActivityEngine;
