const uint8_t NO_OF_PPM_CHANNELS = 6;  //Number of channels being used
const uint16_t DEFAULT_SERVO_VALUE = 1500;  //Default channel value
const uint16_t MIN_SERVO_VALUE = 1150;  //Minimum servo value
const uint16_t MAX_SERVO_VALUE = 1850;  //Minimum servo value
const uint16_t PPM_FRAME_LEN = 22500;  //PPM frame length in microseconds
const uint16_t PPM_PULSE_LEN = 300;  //Pulse length in microseconds
const uint16_t IS_POSITIVE_POLARITY = 0;  //Polarity of the pulses (0 negative and 1 positive)
const uint8_t SIGNAL_PIN = 10;  //Output pin on arduino (use 9 or 9 to use Timer1 for PPM)

volatile uint16_t Ppm[NO_OF_PPM_CHANNELS]; //Array that holds servo values for ppm signal (usually ranges from 1000-2000)
volatile uint16_t PpmIn[NO_OF_PPM_CHANNELS]; //Array that holds servo values for ppm signal after read (usually ranges from 1000-2000)

/*
 * Setup for ppm array values and Serial
 */
void setup() 
{
  Serial.begin(115200); //Begins serial
  Serial.println("SERIAL OPENED");

  //Initialize all channels with default value
  for (uint8_t channel = 0; channel < sizeof(PpmIn)/sizeof(int); channel++) 
  {
    PpmIn[channel] = DEFAULT_SERVO_VALUE;
  }

  /* SETUP PPM */
  pinMode(SIGNAL_PIN, OUTPUT);
  digitalWrite(SIGNAL_PIN, !IS_POSITIVE_POLARITY);  //set the PPM signal pin to the default state (off)

  cli();
  TCCR1A = 0; // set entire TCCR1 register to 0
  TCCR1B = 0;

  OCR1A = 100;  // compare match register, change this
  TCCR1B |= (1 << WGM12);  // turn on CTC mode
  TCCR1B |= (1 << CS11);  // 8 prescaler: 0,5 microseconds at 16mhz
  TIMSK1 |= (1 << OCIE1A); // enable timer compare interrupt
  sei();

  /* SETUP SERIAL */
  //Waits until Serial Port initialized
  while (!Serial) {
    //stub
  }

  Serial.println("SERIAL STARTED");
}

/*
  Main loop

  - takes information for ppm and channel values
  - Updates PPM information
  - Send information through PPM frame
*/
void loop() 
{
  setPPM(1, 1200); //Test code
}


void setPPM(uint8_t channel_num, uint16_t ppm_value) {
  PpmIn[channel_num - 1] = ppm_value;
}

/*
  Creates PPM signal
*/
ISR(TIMER1_COMPA_vect) 
{
  
  static boolean state = true;

  //copy incomming values to ppm array
  memcpy( (void*)Ppm, (void*)PpmIn, sizeof(Ppm));

  TCNT1 = 0;

  if (state) 
  {  
    //start pulse
    digitalWrite(SIGNAL_PIN, IS_POSITIVE_POLARITY);
    OCR1A = PPM_PULSE_LEN * 2;
    state = false;
  }
  else 
  {  
    //end pulse and calculate when to start the next pulse
    static byte cur_chan_numb;
    static unsigned int calc_rest;

    digitalWrite(SIGNAL_PIN, !IS_POSITIVE_POLARITY);
    state = true;

    if (cur_chan_numb >= NO_OF_PPM_CHANNELS) 
    {
      cur_chan_numb = 0;
      calc_rest = calc_rest + PPM_PULSE_LEN;// 
      OCR1A = (PPM_FRAME_LEN - calc_rest) * 2;
      calc_rest = 0;
    }
    else 
    {
      OCR1A = (Ppm[cur_chan_numb] - PPM_PULSE_LEN) * 2;
      calc_rest = calc_rest + Ppm[cur_chan_numb];
      cur_chan_numb++;
    }
  }
}
