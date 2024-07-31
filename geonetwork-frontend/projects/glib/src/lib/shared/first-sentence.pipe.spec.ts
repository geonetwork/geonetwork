import { FirstSentencePipe } from './first-phrase.pipe';

describe('FirstPhrasePipe', () => {
  it('create an instance', () => {
    const pipe = new FirstSentencePipe();
    expect(pipe).toBeTruthy();
  });
});
