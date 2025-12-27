import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Alert, Badge, Button, Col, Row } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import { AUTHORITIES } from 'app/config/constants';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { usePublicNavigation } from 'app/shared/hooks/use-public-navigation';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const { items, loading, hasError } = usePublicNavigation();
  const isViewer = hasAnyAuthority(account?.authorities ?? [], [AUTHORITIES.VIEWER]);
  const isCreator = hasAnyAuthority(account?.authorities ?? [], [AUTHORITIES.CREATOR]);

  return (
    <div className="public-home">
      <Row className="public-home__hero">
        <Col lg="8">
          <h1>Monsterdam</h1>
          <p className="lead">Bienvenido/a. Esta es la navegación principal para visitantes anónimos y viewers, alimentada desde el BFF.</p>
          {isAuthenticated ? (
            <Alert color="success" className="public-home__alert">
              Sesión activa para <strong>{account?.login}</strong>.&nbsp;
              {isViewer && <Badge color="info">Viewer</Badge>}&nbsp;
              {isCreator && <Badge color="secondary">Creator</Badge>}
            </Alert>
          ) : (
            <Alert color="warning" className="public-home__alert">
              Estás navegando como <strong>ANON</strong>. Puedes explorar el contenido y luego iniciar sesión cuando quieras.
            </Alert>
          )}
          <div className="public-home__actions">
            {!isAuthenticated && (
              <Button color="primary" tag={Link} to="/login">
                Iniciar sesión
              </Button>
            )}
            {!isAuthenticated && (
              <Button color="outline-primary" tag={Link} to="/account/register">
                Registrarme
              </Button>
            )}
            {isAuthenticated && (
              <Button color="primary" tag={Link} to="/public/explorar">
                Continuar navegando
              </Button>
            )}
          </div>
        </Col>
      </Row>
      <Row className="public-home__navigation">
        <Col lg="12">
          <h2>Menú principal</h2>
          <p className="text-muted">
            {loading && 'Cargando navegación desde el BFF...'}
            {!loading && hasError && 'Mostrando navegación local mientras se configura el BFF.'}
            {!loading && !hasError && 'Navegación activa desde el BFF.'}
          </p>
          <div className="public-home__cards">
            {items.map(item => (
              <div key={item.id ?? item.path} className="public-home__card">
                <h3>{item.label}</h3>
                {item.description && <p>{item.description}</p>}
                {item.path.startsWith('http') ? (
                  <Button color="link" href={item.path} target="_blank" rel="noopener noreferrer">
                    Abrir
                  </Button>
                ) : (
                  <Button color="link" tag={Link} to={item.path}>
                    Entrar
                  </Button>
                )}
              </div>
            ))}
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default Home;
