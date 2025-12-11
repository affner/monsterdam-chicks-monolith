import React from 'react';
import { MemoryRouter, Route, Routes } from 'react-router';
import { render } from '@testing-library/react';
import { TranslatorContext } from 'react-jhipster';
import configureStore from 'redux-mock-store';
import { Provider } from 'react-redux';

import { AUTHORITIES } from 'app/config/constants';
import PrivateRoute, { hasAnyAuthority } from './private-route';

const TestComp = () => <div>Test</div>;

describe('private-route component', () => {
  beforeAll(() => {
    TranslatorContext.registerTranslations('es', {
      'error.http.403': 'You are not authorized to access this page.',
    });
  });

  const mockStore = configureStore();
  const wrapper = (Elem: JSX.Element, authentication) => {
    const store = mockStore({ authentication });
    return render(
      <Provider store={store}>
        <MemoryRouter>{Elem}</MemoryRouter>
      </Provider>,
    );
  };

  // All tests will go here
  it('Should throw error when falsy children are provided', () => {
    const originalError = console.error;
    console.error = jest.fn();
    expect(() =>
      wrapper(<PrivateRoute>{null}</PrivateRoute>, {
        isAuthenticated: true,
        sessionHasBeenFetched: true,
        account: {
          authorities: [],
        },
      }),
    ).toThrow(Error);
    console.error = originalError;
  });

  it('Should render an error message when the user has no authorities', () => {
    const { container } = wrapper(
      <PrivateRoute>
        <TestComp />
      </PrivateRoute>,
      {
        isAuthenticated: true,
        sessionHasBeenFetched: true,
        account: {
          authorities: [],
        },
      },
    );
    expect(container.innerHTML).toMatch(/<div class="insufficient-authority"><div class="alert alert-danger">.*<\/div><\/div>/);
  });

  it('Should render a route for the component provided when authenticated', () => {
    const { container } = wrapper(
      <PrivateRoute>
        <TestComp />
      </PrivateRoute>,
      {
        isAuthenticated: true,
        sessionHasBeenFetched: true,
        account: {
          authorities: ['ADMIN'],
        },
      },
    );
    expect(container.innerHTML).toEqual('<div>Test</div>');
  });

  it('Should redirect when not authenticated', () => {
    const { container } = wrapper(
      <Routes>
        <Route
          path="/"
          element={
            <PrivateRoute>
              <TestComp />
            </PrivateRoute>
          }
        />
        <Route path="/login" element={<div>Login</div>} />
      </Routes>,
      {
        isAuthenticated: false,
        sessionHasBeenFetched: true,
        account: {
          authorities: ['ADMIN'],
        },
      },
    );
    expect(container.innerHTML).not.toEqual('<div>Test</div>');
    expect(container.innerHTML).toEqual('<div>Login</div>');
  });
});

describe('hasAnyAuthority', () => {
  // All tests will go here
  it('Should return false when authorities is invalid', () => {
    expect(hasAnyAuthority(undefined, undefined)).toEqual(false);
    expect(hasAnyAuthority(null, [])).toEqual(false);
    expect(hasAnyAuthority([], [])).toEqual(false);
    expect(hasAnyAuthority([], [AUTHORITIES.VIEWER])).toEqual(false);
  });

  it('Should return true when authorities is valid and hasAnyAuthorities is empty', () => {
    expect(hasAnyAuthority([AUTHORITIES.VIEWER], [])).toEqual(true);
  });

  it('Should return true when authorities is valid and hasAnyAuthorities contains an authority', () => {
    expect(hasAnyAuthority([AUTHORITIES.VIEWER], [AUTHORITIES.VIEWER])).toEqual(true);
    expect(hasAnyAuthority([AUTHORITIES.VIEWER, AUTHORITIES.CREATOR], [AUTHORITIES.VIEWER])).toEqual(true);
    expect(hasAnyAuthority([AUTHORITIES.VIEWER, AUTHORITIES.CREATOR], [AUTHORITIES.VIEWER, AUTHORITIES.CREATOR])).toEqual(true);
    expect(hasAnyAuthority([AUTHORITIES.VIEWER, AUTHORITIES.CREATOR], [AUTHORITIES.VIEWER, 'ROLE_CREATOR'])).toEqual(true);
    expect(hasAnyAuthority([AUTHORITIES.VIEWER, AUTHORITIES.SUPPORT_ADMIN], [AUTHORITIES.SUPPORT_ADMIN])).toEqual(true);
  });

  it('Should return false when authorities is valid and hasAnyAuthorities does not contain an authority', () => {
    expect(hasAnyAuthority([AUTHORITIES.VIEWER], [AUTHORITIES.SUPPORT_ADMIN])).toEqual(false);
    expect(hasAnyAuthority([AUTHORITIES.VIEWER, AUTHORITIES.CREATOR], ['ROLE_VIEWERS'])).toEqual(false);
    expect(hasAnyAuthority([AUTHORITIES.VIEWER, AUTHORITIES.CREATOR], ['ROLE_CREATORX', 'ROLE_SUPPORT_ADMIN'])).toEqual(false);
  });
});
