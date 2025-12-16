import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './admin-email-configs.reducer';

export const AdminEmailConfigsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const adminEmailConfigsEntity = useAppSelector(state => state.adminEmailConfigs.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="adminEmailConfigsDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.adminEmailConfigs.detail.title">AdminEmailConfigs</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{adminEmailConfigsEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="monsterdamChicksApp.adminEmailConfigs.title">Title</Translate>
            </span>
          </dt>
          <dd>{adminEmailConfigsEntity.title}</dd>
          <dt>
            <span id="subject">
              <Translate contentKey="monsterdamChicksApp.adminEmailConfigs.subject">Subject</Translate>
            </span>
          </dt>
          <dd>{adminEmailConfigsEntity.subject}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="monsterdamChicksApp.adminEmailConfigs.content">Content</Translate>
            </span>
          </dt>
          <dd>{adminEmailConfigsEntity.content}</dd>
          <dt>
            <span id="mailTemplateType">
              <Translate contentKey="monsterdamChicksApp.adminEmailConfigs.mailTemplateType">Mail Template Type</Translate>
            </span>
          </dt>
          <dd>{adminEmailConfigsEntity.mailTemplateType}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.adminEmailConfigs.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {adminEmailConfigsEntity.createdDate ? (
              <TextFormat value={adminEmailConfigsEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.adminEmailConfigs.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {adminEmailConfigsEntity.lastModifiedDate ? (
              <TextFormat value={adminEmailConfigsEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.adminEmailConfigs.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{adminEmailConfigsEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.adminEmailConfigs.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{adminEmailConfigsEntity.lastModifiedBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.adminEmailConfigs.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {adminEmailConfigsEntity.deletedDate ? (
              <TextFormat value={adminEmailConfigsEntity.deletedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/admin-email-configs" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/admin-email-configs/${adminEmailConfigsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AdminEmailConfigsDetail;
