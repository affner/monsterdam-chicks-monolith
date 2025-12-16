import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate, byteSize, openFile } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './identity-document.reducer';

export const IdentityDocumentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const identityDocumentEntity = useAppSelector(state => state.identityDocument.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="identityDocumentDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.identityDocument.detail.title">IdentityDocument</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{identityDocumentEntity.id}</dd>
          <dt>
            <span id="documentName">
              <Translate contentKey="monsterdamChicksApp.identityDocument.documentName">Document Name</Translate>
            </span>
          </dt>
          <dd>{identityDocumentEntity.documentName}</dd>
          <dt>
            <span id="documentDescription">
              <Translate contentKey="monsterdamChicksApp.identityDocument.documentDescription">Document Description</Translate>
            </span>
          </dt>
          <dd>{identityDocumentEntity.documentDescription}</dd>
          <dt>
            <span id="documentStatus">
              <Translate contentKey="monsterdamChicksApp.identityDocument.documentStatus">Document Status</Translate>
            </span>
          </dt>
          <dd>{identityDocumentEntity.documentStatus}</dd>
          <dt>
            <span id="documentType">
              <Translate contentKey="monsterdamChicksApp.identityDocument.documentType">Document Type</Translate>
            </span>
          </dt>
          <dd>{identityDocumentEntity.documentType}</dd>
          <dt>
            <span id="fileDocument">
              <Translate contentKey="monsterdamChicksApp.identityDocument.fileDocument">File Document</Translate>
            </span>
          </dt>
          <dd>
            {identityDocumentEntity.fileDocument ? (
              <div>
                {identityDocumentEntity.fileDocumentContentType ? (
                  <a onClick={openFile(identityDocumentEntity.fileDocumentContentType, identityDocumentEntity.fileDocument)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {identityDocumentEntity.fileDocumentContentType}, {byteSize(identityDocumentEntity.fileDocument)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="fileDocumentS3Key">
              <Translate contentKey="monsterdamChicksApp.identityDocument.fileDocumentS3Key">File Document S 3 Key</Translate>
            </span>
          </dt>
          <dd>{identityDocumentEntity.fileDocumentS3Key}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.identityDocument.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {identityDocumentEntity.createdDate ? (
              <TextFormat value={identityDocumentEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.identityDocument.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {identityDocumentEntity.lastModifiedDate ? (
              <TextFormat value={identityDocumentEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.identityDocument.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{identityDocumentEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.identityDocument.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{identityDocumentEntity.lastModifiedBy}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.identityDocument.review">Review</Translate>
          </dt>
          <dd>{identityDocumentEntity.review ? identityDocumentEntity.review.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/identity-document" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/identity-document/${identityDocumentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default IdentityDocumentDetail;
