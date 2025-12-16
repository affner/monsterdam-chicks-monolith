import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { DocumentStatus } from 'app/shared/model/enumerations/document-status.model';
import { ReviewStatus } from 'app/shared/model/enumerations/review-status.model';
import { createEntity, getEntity, reset, updateEntity } from './identity-document-review.reducer';

export const IdentityDocumentReviewUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const identityDocumentReviewEntity = useAppSelector(state => state.identityDocumentReview.entity);
  const loading = useAppSelector(state => state.identityDocumentReview.loading);
  const updating = useAppSelector(state => state.identityDocumentReview.updating);
  const updateSuccess = useAppSelector(state => state.identityDocumentReview.updateSuccess);
  const documentStatusValues = Object.keys(DocumentStatus);
  const reviewStatusValues = Object.keys(ReviewStatus);

  const handleClose = () => {
    navigate(`/identity-document-review${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.resolutionDate = convertDateTimeToServer(values.resolutionDate);
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    if (values.ticketId !== undefined && typeof values.ticketId !== 'number') {
      values.ticketId = Number(values.ticketId);
    }

    const entity = {
      ...identityDocumentReviewEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          resolutionDate: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          documentStatus: 'PENDING',
          reviewStatus: 'REVIEWING',
          ...identityDocumentReviewEntity,
          resolutionDate: convertDateTimeFromServer(identityDocumentReviewEntity.resolutionDate),
          createdDate: convertDateTimeFromServer(identityDocumentReviewEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(identityDocumentReviewEntity.lastModifiedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.identityDocumentReview.home.createOrEditLabel" data-cy="IdentityDocumentReviewCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.identityDocumentReview.home.createOrEditLabel">
              Create or edit a IdentityDocumentReview
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="identity-document-review-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.identityDocumentReview.documentStatus')}
                id="identity-document-review-documentStatus"
                name="documentStatus"
                data-cy="documentStatus"
                type="select"
              >
                {documentStatusValues.map(documentStatus => (
                  <option value={documentStatus} key={documentStatus}>
                    {translate(`monsterdamChicksApp.DocumentStatus.${documentStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.identityDocumentReview.resolutionDate')}
                id="identity-document-review-resolutionDate"
                name="resolutionDate"
                data-cy="resolutionDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.identityDocumentReview.reviewStatus')}
                id="identity-document-review-reviewStatus"
                name="reviewStatus"
                data-cy="reviewStatus"
                type="select"
              >
                {reviewStatusValues.map(reviewStatus => (
                  <option value={reviewStatus} key={reviewStatus}>
                    {translate(`monsterdamChicksApp.ReviewStatus.${reviewStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.identityDocumentReview.createdDate')}
                id="identity-document-review-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.identityDocumentReview.lastModifiedDate')}
                id="identity-document-review-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.identityDocumentReview.createdBy')}
                id="identity-document-review-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.identityDocumentReview.lastModifiedBy')}
                id="identity-document-review-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.identityDocumentReview.ticketId')}
                id="identity-document-review-ticketId"
                name="ticketId"
                data-cy="ticketId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/identity-document-review" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default IdentityDocumentReviewUpdate;
