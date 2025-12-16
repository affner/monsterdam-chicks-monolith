import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './content-package.reducer';

export const ContentPackageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const contentPackageEntity = useAppSelector(state => state.contentPackage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="contentPackageDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.contentPackage.detail.title">ContentPackage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{contentPackageEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="monsterdamChicksApp.contentPackage.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{contentPackageEntity.amount}</dd>
          <dt>
            <span id="videoCount">
              <Translate contentKey="monsterdamChicksApp.contentPackage.videoCount">Video Count</Translate>
            </span>
          </dt>
          <dd>{contentPackageEntity.videoCount}</dd>
          <dt>
            <span id="imageCount">
              <Translate contentKey="monsterdamChicksApp.contentPackage.imageCount">Image Count</Translate>
            </span>
          </dt>
          <dd>{contentPackageEntity.imageCount}</dd>
          <dt>
            <span id="isPaidContent">
              <Translate contentKey="monsterdamChicksApp.contentPackage.isPaidContent">Is Paid Content</Translate>
            </span>
          </dt>
          <dd>{contentPackageEntity.isPaidContent ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.contentPackage.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {contentPackageEntity.createdDate ? (
              <TextFormat value={contentPackageEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.contentPackage.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {contentPackageEntity.lastModifiedDate ? (
              <TextFormat value={contentPackageEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.contentPackage.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{contentPackageEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.contentPackage.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{contentPackageEntity.lastModifiedBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.contentPackage.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {contentPackageEntity.deletedDate ? (
              <TextFormat value={contentPackageEntity.deletedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="messageId">
              <Translate contentKey="monsterdamChicksApp.contentPackage.messageId">Message Id</Translate>
            </span>
          </dt>
          <dd>{contentPackageEntity.messageId}</dd>
          <dt>
            <span id="postId">
              <Translate contentKey="monsterdamChicksApp.contentPackage.postId">Post Id</Translate>
            </span>
          </dt>
          <dd>{contentPackageEntity.postId}</dd>
        </dl>
        <Button tag={Link} to="/content-package" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/content-package/${contentPackageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ContentPackageDetail;
