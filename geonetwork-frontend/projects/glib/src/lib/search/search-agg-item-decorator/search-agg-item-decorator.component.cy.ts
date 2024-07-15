import { SearchAggItemDecoratorComponent } from './search-agg-item-decorator.component';
import { signal } from '@angular/core';

describe('SearchAggItemDecoratorComponent', () => {

  let bucket = signal({
    key: 'key',
  });

  it('should set proper icon based on bucket key', () => {
    let aggregationConfig = signal({
      meta: {
        decorator: {
          type: 'icon',
          prefix: 'fa fa-',
          map: {
            key: 'icon',
          },
        },
      },
    });

    cy.mount(SearchAggItemDecoratorComponent, {
      componentProperties: {
        bucket: bucket,
        aggregationConfig: aggregationConfig,
      },
    });

    cy.get('i').should('have.class', aggregationConfig().meta.decorator.prefix + aggregationConfig().meta.decorator.map.key;
  });

  it('should set proper image based on bucket key', () => {
    let aggregationConfig = signal({
      meta: {
        decorator: {
          type: 'img',
          map: {
            key: 'https://live.staticflickr.com/1561/24270793842_f32d495613_b.jpg',
          },
        },
      },
    });

    let bucket = signal({
      key: 'key',
    });
    cy.mount(SearchAggItemDecoratorComponent, {
      componentProperties: {
        bucket: bucket,
        aggregationConfig: aggregationConfig,
      },
    });

    cy.get('div img').should('have.attr', 'src', aggregationConfig().meta.decorator.map.key);
  });

});
