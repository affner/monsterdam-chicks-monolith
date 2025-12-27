import React from 'react';
import { useParams } from 'react-router-dom';
import { Button, Card, CardBody, CardText, CardTitle } from 'reactstrap';

import { usePublicNavigation } from 'app/shared/hooks/use-public-navigation';

const PublicPage = () => {
  const { section } = useParams<'section'>();
  const { items } = usePublicNavigation();

  const current = items.find(item => item.path.endsWith(`/${section}`));

  return (
    <div className="public-page">
      <Card>
        <CardBody>
          <CardTitle tag="h2">{current?.label ?? 'Sección pública'}</CardTitle>
          <CardText>{current?.description ?? 'Esta sección está lista para conectarse con el contenido público del BFF.'}</CardText>
          <Button color="primary" href="/">
            Volver a inicio
          </Button>
        </CardBody>
      </Card>
    </div>
  );
};

export default PublicPage;
