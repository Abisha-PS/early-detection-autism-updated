/**
 * MASCOT ENGINE: Pip the Penguin
 * Modern, child-friendly vocal guidance using Web Speech Synthesis and Web Audio API synthesizer.
 * Includes sensory-friendly mute controls and playful sound effects.
 */

class MascotGuide {
    constructor(elementId = 'mascotSpeech', avatarId = 'mascotAvatar') {
        this.speechBubble = document.getElementById(elementId);
        this.avatar = document.getElementById(avatarId);
        this.synth = window.speechSynthesis;
        this.audioCtx = null;
        this.isMuted = false;
        this.currentLanguage = 'en';
        this.initAudio();
    }

    initAudio() {
        try {
            const AudioContext = window.AudioContext || window.webkitAudioContext;
            this.audioCtx = new AudioContext();
        } catch (e) {
            console.warn('Web Audio API not supported on this browser.');
        }
    }

    setLanguage(lang) {
        this.currentLanguage = lang || 'en';
    }

    toggleMute() {
        this.isMuted = !this.isMuted;
        if (this.isMuted && this.synth) {
            this.synth.cancel();
        }
        return this.isMuted;
    }

    speak(text, callback) {
        if (this.speechBubble) {
            this.speechBubble.innerHTML = text;
        }

        if (this.isMuted || !this.synth) {
            if (callback) callback();
            return;
        }

        // Cancel previous speech to avoid queue pileup
        this.synth.cancel();

        const utterance = new SpeechSynthesisUtterance(text);
        utterance.rate = 0.95; // child-friendly cadence
        utterance.pitch = 1.25; // cheerful high pitch

        // Select voice matching preferred language or friendly natural voice
        const voices = this.synth.getVoices();
        if (voices.length > 0) {
            let childVoice = null;
            if (this.currentLanguage === 'ml') {
                childVoice = voices.find(v => v.lang.startsWith('ml'));
            }
            if (!childVoice) {
                childVoice = voices.find(v => v.lang.startsWith('en') && (v.name.includes('Natural') || v.name.includes('Female') || v.name.includes('Google') || v.name.includes('Samantha')));
            }
            if (childVoice) {
                utterance.voice = childVoice;
            }
        }

        if (this.avatar) {
            this.avatar.classList.add('mascot-speaking');
        }

        utterance.onend = () => {
            if (this.avatar) {
                this.avatar.classList.remove('mascot-speaking');
            }
            if (callback) callback();
        };

        utterance.onerror = () => {
            if (this.avatar) {
                this.avatar.classList.remove('mascot-speaking');
            }
            if (callback) callback();
        };

        this.synth.speak(utterance);
    }

    // Play pleasant, warm game audio feedback synthesized dynamically
    playTone(type = 'pop') {
        if (this.isMuted || !this.audioCtx) return;
        if (this.audioCtx.state === 'suspended') {
            this.audioCtx.resume();
        }

        const now = this.audioCtx.currentTime;

        if (type === 'pop') {
            const osc = this.audioCtx.createOscillator();
            const gain = this.audioCtx.createGain();
            osc.connect(gain);
            gain.connect(this.audioCtx.destination);
            osc.type = 'sine';
            osc.frequency.setValueAtTime(380, now);
            osc.frequency.exponentialRampToValueAtTime(850, now + 0.10);
            gain.gain.setValueAtTime(0.35, now);
            gain.gain.exponentialRampToValueAtTime(0.001, now + 0.11);
            osc.start(now);
            osc.stop(now + 0.11);
        } else if (type === 'chime' || type === 'success') {
            [523.25, 659.25, 783.99].forEach((freq, i) => { // C5, E5, G5
                const osc = this.audioCtx.createOscillator();
                const gain = this.audioCtx.createGain();
                osc.connect(gain);
                gain.connect(this.audioCtx.destination);
                osc.type = 'triangle';
                const startTime = now + (i * 0.09);
                osc.frequency.setValueAtTime(freq, startTime);
                gain.gain.setValueAtTime(0.25, startTime);
                gain.gain.exponentialRampToValueAtTime(0.001, startTime + 0.35);
                osc.start(startTime);
                osc.stop(startTime + 0.35);
            });
        } else if (type === 'sparkle') {
            [880, 1046.5, 1318.5, 1567.98].forEach((freq, i) => {
                const osc = this.audioCtx.createOscillator();
                const gain = this.audioCtx.createGain();
                osc.connect(gain);
                gain.connect(this.audioCtx.destination);
                osc.type = 'sine';
                const startTime = now + (i * 0.07);
                osc.frequency.setValueAtTime(freq, startTime);
                gain.gain.setValueAtTime(0.2, startTime);
                gain.gain.exponentialRampToValueAtTime(0.001, startTime + 0.25);
                osc.start(startTime);
                osc.stop(startTime + 0.25);
            });
        } else if (type === 'fanfare') {
            const chords = [
                { f: 523.25, t: 0 },
                { f: 659.25, t: 0.12 },
                { f: 783.99, t: 0.24 },
                { f: 1046.50, t: 0.38 }
            ];
            chords.forEach(c => {
                const osc = this.audioCtx.createOscillator();
                const gain = this.audioCtx.createGain();
                osc.connect(gain);
                gain.connect(this.audioCtx.destination);
                osc.type = 'triangle';
                const startTime = now + c.t;
                osc.frequency.setValueAtTime(c.f, startTime);
                gain.gain.setValueAtTime(0.3, startTime);
                gain.gain.exponentialRampToValueAtTime(0.001, startTime + 0.55);
                osc.start(startTime);
                osc.stop(startTime + 0.55);
            });
        } else if (type === 'rocket') {
            const osc = this.audioCtx.createOscillator();
            const gain = this.audioCtx.createGain();
            osc.connect(gain);
            gain.connect(this.audioCtx.destination);
            osc.type = 'sawtooth';
            osc.frequency.setValueAtTime(120, now);
            osc.frequency.exponentialRampToValueAtTime(900, now + 0.6);
            gain.gain.setValueAtTime(0.25, now);
            gain.gain.exponentialRampToValueAtTime(0.001, now + 0.65);
            osc.start(now);
            osc.stop(now + 0.65);
        } else if (type === 'woof') {
            const osc = this.audioCtx.createOscillator();
            const gain = this.audioCtx.createGain();
            osc.connect(gain);
            gain.connect(this.audioCtx.destination);
            osc.type = 'triangle';
            osc.frequency.setValueAtTime(220, now);
            osc.frequency.exponentialRampToValueAtTime(140, now + 0.18);
            gain.gain.setValueAtTime(0.35, now);
            gain.gain.exponentialRampToValueAtTime(0.01, now + 0.2);
            osc.start(now);
            osc.stop(now + 0.2);
        } else if (type === 'meow') {
            const osc = this.audioCtx.createOscillator();
            const gain = this.audioCtx.createGain();
            osc.connect(gain);
            gain.connect(this.audioCtx.destination);
            osc.type = 'sine';
            osc.frequency.setValueAtTime(400, now);
            osc.frequency.linearRampToValueAtTime(700, now + 0.25);
            osc.frequency.exponentialRampToValueAtTime(320, now + 0.45);
            gain.gain.setValueAtTime(0.25, now);
            gain.gain.exponentialRampToValueAtTime(0.001, now + 0.5);
            osc.start(now);
            osc.stop(now + 0.5);
        } else if (type === 'note') {
            const osc = this.audioCtx.createOscillator();
            const gain = this.audioCtx.createGain();
            osc.connect(gain);
            gain.connect(this.audioCtx.destination);
            osc.type = 'sine';
            osc.frequency.setValueAtTime(587.33, now); // D5
            gain.gain.setValueAtTime(0.3, now);
            gain.gain.exponentialRampToValueAtTime(0.001, now + 0.3);
            osc.start(now);
            osc.stop(now + 0.3);
        }
    }
}

window.MascotGuide = MascotGuide;
