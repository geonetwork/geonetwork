import { FirstSentencePipe } from './first-sentence.pipe';

describe('FirstPhrasePipe', () => {
  it('create an instance', () => {
    const pipe = new FirstSentencePipe();
    expect(pipe).toBeTruthy();
  });
});
