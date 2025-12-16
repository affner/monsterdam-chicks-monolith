import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './help-subcategory.reducer';

export const HelpSubcategoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const helpSubcategoryEntity = useAppSelector(state => state.helpSubcategory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="helpSubcategoryDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.helpSubcategory.detail.title">HelpSubcategory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{helpSubcategoryEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="monsterdamChicksApp.helpSubcategory.name">Name</Translate>
            </span>
          </dt>
          <dd>{helpSubcategoryEntity.name}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.helpSubcategory.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {helpSubcategoryEntity.createdDate ? (
              <TextFormat value={helpSubcategoryEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.helpSubcategory.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {helpSubcategoryEntity.lastModifiedDate ? (
              <TextFormat value={helpSubcategoryEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.helpSubcategory.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{helpSubcategoryEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.helpSubcategory.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{helpSubcategoryEntity.lastModifiedBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.helpSubcategory.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {helpSubcategoryEntity.deletedDate ? (
              <TextFormat value={helpSubcategoryEntity.deletedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.helpSubcategory.category">Category</Translate>
          </dt>
          <dd>{helpSubcategoryEntity.category ? helpSubcategoryEntity.category.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/help-subcategory" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/help-subcategory/${helpSubcategoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HelpSubcategoryDetail;
